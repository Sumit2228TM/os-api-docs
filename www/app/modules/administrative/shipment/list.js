
angular.module('os.administrative.shipment.list', ['os.administrative.models'])
  .controller('ShipmentListCtrl', function(
    $scope, $state, $translate, Shipment, Site, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getShipmentsCount, recordsPerPage: 50});
      $scope.filterOpts = Util.filterOpts({maxResults: pagerOpts.recordsPerPage + 1, includeStats: true});
      $scope.emptyState = {
        empty: true,
        loading: true,
        emptyMessage: 'shipments.empty_list',
        loadingMessage: 'shipments.loading_list'
      };

      loadShipments($scope.filterOpts);
      loadStatuses();
      Util.filter($scope, 'filterOpts', loadShipments);
    }

    function loadShipments(filterOpts) {
      $scope.emptyState.loading = true;
      Shipment.query(filterOpts).then(
        function(result) {
          $scope.emptyState.loading = false;
          $scope.emptyState.empty = result.length <= 0;
          $scope.shipments = result;
          pagerOpts.refreshOpts(result);
        }
      );
    }

    function loadStatuses() {
      $scope.statuses = [ {name: 'Shipped'}, {name: 'Received'}, {name: 'Pending'} ];
      $translate('shipments.statuses.Shipped').then(
        function() {
          angular.forEach($scope.statuses,
            function(status) {
              status.caption = $translate.instant('shipments.statuses.' + status.name);
            }
          );
        }
      );
    }
 
    function getShipmentsCount() {
      return Shipment.getCount($scope.filterOpts);
    }

    $scope.showShipmentOverview = function(shipment) {
      $state.go('shipment-detail.overview', {shipmentId: shipment.id});
    };
    
    $scope.onInstituteSelect = function() {
      $scope.filterOpts.recvSite = undefined;
    }

    $scope.pageSizeChanged = function() {
      $scope.filterOpts.maxResults = $scope.pagerOpts.recordsPerPage + 1;
    }

    init();
  });
