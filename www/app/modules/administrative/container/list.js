angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Util, DeleteUtil, ListPagerOpts, CheckList) {

    var pagerOpts, filterOpts, ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getContainersCount});
      filterOpts = $scope.containerFilterOpts = Util.filterOpts({
        orderByStarred: true,
        maxResults: pagerOpts.recordsPerPage + 1,
        includeStats: true,
        topLevelContainers: true
      });

      ctx = $scope.ctx = {
        containerList: [],
        exportDetail: { objectType: 'storageContainer' },
        emptyState: {
          loading: true,
          empty: true,
          loadingMessage: 'container.loading_list',
          emptyMessage: 'container.empty_list'
        }
      };

      loadContainers($scope.containerFilterOpts);
      Util.filter($scope, 'containerFilterOpts', loadContainers, ['maxResults', 'includeStats', 'topLevelContainers']);
    }

    function loadContainers(filterOpts) {
      ctx.emptyState.loading = true;
      Container.list(filterOpts).then(
        function(containers) {
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = containers.length <= 0;
          pagerOpts.refreshOpts(containers);

          angular.forEach(containers,
            function(container) {
              if (container.capacity) {
                container.utilisation = Math.round(container.storedSpecimens / container.capacity * 100);
              }
            }
          );

          $scope.ctx.containerList = containers;
          $scope.ctx.checkList = new CheckList(containers);
        }
      );
    }

    function getContainerIds(containers) {
      return containers.map(function(container) { return container.id; });
    }

    function getContainersCount() {
      return Container.getCount($scope.containerFilterOpts);
    }

    $scope.showContainerDetail = function(container) {
      $state.go('container-detail.locations', {containerId: container.id});
    };

    $scope.deleteContainers = function() {
      var containers = $scope.ctx.checkList.getSelectedItems();

      var opts = {
        confirmDelete:  'container.delete_containers',
        successMessage: 'container.containers_deleted',
        onBulkDeletion: function() {
          loadContainers($scope.containerFilterOpts);
        }
      }

      DeleteUtil.bulkDelete({bulkDelete: Container.bulkDelete}, getContainerIds(containers), opts);
    }

    $scope.pageSizeChanged = function() {
      filterOpts.maxResults = pagerOpts.recordsPerPage + 1;
    }

    $scope.toggleStar = function(container) {
      var q = container.starred ? container.unstar() : container.star();
      q.then(
        function(result) {
          if (result.status == true) {
            container.starred = !container.starred;
          }
        }
      );
    }

    init();
  });
