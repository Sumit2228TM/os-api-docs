
angular.module('os.administrative.order')
  .controller('HomeOrderListCtrl', function($scope, DistributionOrder) {
    var ctx;

    function init(opts) {
      ctx = $scope.ctx = {
        defList: undefined,
        orders: []
      };

      $scope.$watch('opts.searchTerm', function(newVal) { loadOrders(newVal); });
    }

    function loadOrders(searchTerm) {
      if (!searchTerm && ctx.defList) {
        ctx.orders = ctx.defList;
        return;
      }

      DistributionOrder.query({query: searchTerm, orderByStarred: true, maxResults: 25}).then(
        function(orders) {
          ctx.orders = orders;
          if (!searchTerm) {
            ctx.defList = orders;
          }
        }
      );
    }

    $scope.init = init;
  });
