
angular.module('os.administrative.shipment')
  .controller('HomeShipmentListCtrl', function($scope, Shipment) {
    var ctx;

    function init(opts) {
      ctx = $scope.ctx = {
        defList: undefined,
        shipments: []
      };

      $scope.$watch('opts.searchTerm', function(newVal) { loadShipments(newVal); });
    }

    function loadShipments(searchTerm) {
      if (!searchTerm && ctx.defList) {
        ctx.shipments = ctx.defList;
        return;
      }

      Shipment.query({name: searchTerm, orderByStarred: true, maxResults: 25}).then(
        function(shipments) {
          ctx.shipments = shipments;
          if (!searchTerm) {
            ctx.defList = shipments;
          }
        }
      );
    }

    $scope.init = init;
  });
