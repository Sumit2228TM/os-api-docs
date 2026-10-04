angular.module('os.administrative.containertype.list', ['os.administrative.models'])
  .controller('ContainerTypeListCtrl', function($scope, $state, CheckList, ContainerType, Util, DeleteUtil, ListPagerOpts) {

    var pagerOpts, ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getContainerTypesCount});
      ctx = $scope.ctx = {
        exportDetail: {objectType: 'storageContainerType'},
        filterOpts: {maxResults: pagerOpts.recordsPerPage + 1},
        emptyState: {
          loading: true,
          empty: true,
          loadingMessage: 'container_type.loading_list',
          emptyMessage: 'container_type.empty_list'
        }
      };

      loadContainerTypes(ctx.filterOpts);
      Util.filter($scope, 'ctx.filterOpts', loadContainerTypes);
    }

    function loadContainerTypes(filterOpts) {
      ctx.emptyState.loading = true;

      var q = ContainerType.query(filterOpts);
      if (Object.keys(ctx.filterOpts).length == 1) {
        ctx.defTypesQ = q;
      }

      q.then(
        function(containerTypes) {
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = (containerTypes.length <= 0);
          pagerOpts.refreshOpts(containerTypes);

          $scope.containerTypes = containerTypes;
          $scope.ctx.checkList = new CheckList(containerTypes);
        }
      );
    };

    function getContainerTypesCount() {
      return ContainerType.getCount(ctx.filterOpts);
    }

    function getTypeIds(types) {
      return types.map(function(t) { return t.id; });
    }

    $scope.deleteTypes = function() {
      var types = $scope.ctx.checkList.getSelectedItems();
      var opts = {
        confirmDelete:  'container_type.delete_types',
        successMessage: 'container_type.types_deleted',
        onBulkDeletion: function() {
          loadContainerTypes(ctx.filterOpts);
        }
      }

      DeleteUtil.bulkDelete({bulkDelete: ContainerType.bulkDelete}, getTypeIds(types), opts);
    }

    $scope.loadTypes = function(search) {
      if (ctx.defTypes && (ctx.defTypes.length < 100 || !search)) {
        $scope.canHolds = ctx.defTypes;
        return;
      }

      var q;
      if (!search && ctx.defTypesQ) {
        q = ctx.defTypesQ;
      } else {
        q = ContainerType.query({name: search});
      }

      q.then(
        function(types) {
          if (!search) {
            ctx.defTypes = types;
          }

          $scope.canHolds = types;
        }
      );
    }

    $scope.showContainerTypeOverview = function(containerType) {
      $state.go('container-type-detail.overview', {containerTypeId: containerType.id});
    };

    $scope.pageSizeChanged = function() {
      ctx.filterOpts.maxResults = pagerOpts.recordsPerPage + 1;
    }

    init();
  });

