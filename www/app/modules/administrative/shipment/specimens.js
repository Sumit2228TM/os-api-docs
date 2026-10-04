angular.module('os.administrative.shipment')
  .controller('ShipmentSpecimensCtrl', function($scope, shipment, ShipmentUtil) {
  
    var ctx = {
      totalItems: 0,
      currPage: 1,
      itemsPerPage: 50,
      shipmentSpmns: []
    };

    function init() {
      $scope.ctx = ctx;
      ctx.emptyState = {
        empty: true,
        loading: true,
        emptyMessage: 'specimens.empty_list',
        loadingMessage: 'specimens.loading_list'
      };

      loadSpecimens(); 
      $scope.$watch('ctx.currPage', loadSpecimens);
    }

    function loadSpecimens() {
      var startAt     = (ctx.currPage - 1) * ctx.itemsPerPage;
      var maxResults  = ctx.itemsPerPage + 1;

      ctx.emptyState.loading = true;
      shipment.getSpecimens(startAt, maxResults, ctx.orderBy, ctx.direction).then(
        function(shipmentSpmns) {
          ctx.totalItems = (ctx.currPage - 1) * ctx.itemsPerPage + shipmentSpmns.length;

          ctx.emptyState.loading = false;
          ctx.emptyState.empty = (ctx.totalItems <= 0);

          if (shipmentSpmns.length >= maxResults) {
            shipmentSpmns.splice(shipmentSpmns.length - 1, 1);
          }

          ctx.shipmentSpmns = shipmentSpmns;
          angular.extend(ctx, ShipmentUtil.hasPpidAndExtIds(shipmentSpmns));
        }
      );
    }

    $scope.sortBy = function(attr) {
      if (ctx.orderBy == attr) {
        ctx.direction = (ctx.direction == 'asc' ? 'desc' : 'asc');
      } else {
        ctx.orderBy = attr;
        ctx.direction = 'asc';
      }

      loadSpecimens();
    }

    init();
  });
