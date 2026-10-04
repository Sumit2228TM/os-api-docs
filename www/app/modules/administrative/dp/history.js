
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, $sce, distributionProtocol, DistributionProtocol) {
    $scope.exportUrl = $sce.trustAsResourceUrl(distributionProtocol.historyExportUrl());
  
    var ctx;

    function init() {
      ctx = $scope.ctx = {
        orders: [],
        emptyState: {
          empty: true,
          loading: true,
          emptyMessage: 'orders.empty_list',
          loadingMessage: 'orders.loading_list'
        }
      },

      loadOrders();
    }

    function loadOrders() {
      var opts = {
        dpId: distributionProtocol.id,
        groupBy: 'specimenType,anatomicSite,pathologyStatus'
      };
      
      ctx.emptyState.loading = true;
      DistributionProtocol.getOrders(opts).then(
        function(orders) {
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = orders.length <= 0;
          ctx.orders = orders;
        }
      );
    }
    
    init();
  });
