
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function(
    $scope, $state, DistributionProtocol, Util, DeleteUtil,
    PvManager, ListPagerOpts, CheckList) {

    var pagerOpts, filterOpts, ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getDpsCount});
      filterOpts = $scope.dpFilterOpts = Util.filterOpts({
        orderByStarred: true,
        includeStats: true,
        maxResults: pagerOpts.recordsPerPage + 1
      });

      ctx = $scope.ctx = {
        exportDetail: {objectType: 'distributionProtocol'},
        emptyState: {
          empty: true,
          loading: true,
          emptyMessage: 'dp.empty_list',
          loadingMessage: 'dp.loading_list'
        }
      };

      loadDps($scope.dpFilterOpts);
      Util.filter($scope, 'dpFilterOpts', loadDps);
      loadActivityStatuses();
    }
    
    function loadDps(filterOpts) {
      ctx.emptyState.loading = true;
      DistributionProtocol.query(filterOpts).then(
        function(dps) {
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = dps.length <= 0;
          pagerOpts.refreshOpts(dps);

          $scope.distributionProtocols = dps;
          $scope.ctx.checkList = new CheckList(dps);
        }
      );
    }

    function getDpsCount() {
      return DistributionProtocol.getCount($scope.dpFilterOpts);
    }

    function loadActivityStatuses () {
      PvManager.loadPvs('activity-status').then(
        function (result) {
          $scope.activityStatuses = [];
          angular.forEach(result, function (status) {
            if (status != 'Disabled' && status != 'Pending') {
              $scope.activityStatuses.push(status);
            }
          });
        }
      );
    }

    function getDpIds(dps) {
      return dps.map(function(dp) { return dp.id; });
    }

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId: distributionProtocol.id});
    };

    $scope.deleteDps = function() {
      var dps = $scope.ctx.checkList.getSelectedItems();
      var opts = {
        confirmDelete:  'dp.delete_dps',
        successMessage: 'dp.dps_deleted',
        onBulkDeletion: function() {
          loadDps($scope.dpFilterOpts);
        }
      }

      DeleteUtil.bulkDelete({bulkDelete: DistributionProtocol.bulkDelete}, getDpIds(dps), opts);
    }

    $scope.pageSizeChanged = function() {
      filterOpts.maxResults = pagerOpts.recordsPerPage + 1;
    }

    $scope.toggleStar = function(dp) {
      var q = dp.starred ? dp.unstar() : dp.star();
      q.then(
        function(result) {
          if (result.status == true) {
            dp.starred = !dp.starred;
          }
        }
      );
    }

    init();
  });
