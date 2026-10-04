angular.module('os.administrative.shipment')
  .controller('ShipmentContainersCtrl', function($scope, shipment) {
  
    var ctx = {
      totalItems: 0,
      currPage: 1,
      itemsPerPage: 25,
      shipmentContainers: [],
      loading: false
    };

    function init() {
      $scope.ctx = ctx;
      ctx.emptyState = {
        empty: true,
        loading: true,
        emptyMessage: 'container.empty_list',
        loadingMessage: 'container.loading_list'
      };

      loadContainers(); 
      $scope.$watch('ctx.currPage', loadContainers);
    }

    function loadContainers() {
      var startAt     = (ctx.currPage - 1) * ctx.itemsPerPage;
      var maxResults  = ctx.itemsPerPage + 1;
      ctx.emptyState.loading = true;
      shipment.getContainers(startAt, maxResults, ctx.orderBy, ctx.direction).then(
        function(shipmentContainers) {
          ctx.totalItems = (ctx.currPage - 1) * ctx.itemsPerPage + shipmentContainers.length;
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = (ctx.totalItems <= 0);

          if (shipmentContainers.length >= maxResults) {
            shipmentContainers.splice(shipmentContainers.length - 1, 1);
          }

          ctx.shipmentContainers = shipmentContainers;
          ctx.loading = false;
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

      loadContainers();
    }

    init();
  });
