angular.module('os.administrative.institute.list', ['os.administrative.models'])
  .controller('InstituteListCtrl', function($scope, $state, Institute, Util, DeleteUtil, ListPagerOpts, CheckList) {

    var pagerOpts, filterOpts, ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getInstitutesCount});
      filterOpts = $scope.instituteFilterOpts =
        Util.filterOpts({includeStats: true, maxResults: pagerOpts.recordsPerPage + 1});

      ctx = $scope.ctx = {
        exportDetail: {objectType: 'institute'},
        emptyState: {
          empty: true,
          loading: true,
          emptyMessage: 'institute.empty_list',
          loadingMessage: 'institute.loading_list'
        }
      };

      loadInstitutes($scope.instituteFilterOpts);
      Util.filter($scope, 'instituteFilterOpts', loadInstitutes);
    }

    function loadInstitutes(filterOpts) {
      ctx.emptyState.loading = true;
      Institute.query(filterOpts).then(
        function(instituteList) {
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = instituteList.length <= 0;
          pagerOpts.refreshOpts(instituteList);
          $scope.instituteList = instituteList;
          $scope.ctx.checkList = new CheckList(instituteList);
        }
      );
    }

    function getInstituteIds(institutes) {
      return institutes.map(function(institute) { return institute.id; });
    }

    function getInstitutesCount() {
      return Institute.getCount($scope.instituteFilterOpts);
    }

    $scope.showInstituteOverview = function(institute) {
      $state.go('institute-detail.overview', {instituteId: institute.id});
    };

    $scope.deleteInstitutes = function() {
      var institutes = $scope.ctx.checkList.getSelectedItems();

      var opts = {
        confirmDelete:  'institute.delete_institutes',
        successMessage: 'institute.institutes_deleted',
        onBulkDeletion: function() {
          loadInstitutes($scope.instituteFilterOpts);
        }
      }

      DeleteUtil.bulkDelete({bulkDelete: Institute.bulkDelete}, getInstituteIds(institutes), opts);
    }

    $scope.pageSizeChanged = function() {
      filterOpts.maxResults = pagerOpts.recordsPerPage + 1;
    }

    init();
  });
