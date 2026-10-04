angular.module('os.administrative.shipment.receive', ['os.administrative.models'])
  .controller('ShipmentReceiveCtrl', function(
    $scope, $state, $filter, shipment, shipmentItems, isSpmnRelabelingAllowed, ShipmentUtil, Specimen, Container) {

    var ctx;

    function init() {
      ctx = $scope.ctx = {
        relabelSpmns: isSpmnRelabelingAllowed,
        state: shipment.status == 'Shipped' ? 'RECV_SHIPMENT' : 'RECV_EDIT',
        orderBy: '',
        direction: ''
      };

      var attrs = getItemAttrs();
      angular.forEach(shipmentItems,
        function(item) {
          item[attrs.itemKey] = attrs.newItem(item[attrs.itemKey]);
        }
      );
      shipment[attrs.collName] = shipmentItems;

      $scope.shipment = shipment;
      $scope.spmnShipment = shipment.isSpecimenShipment();
      if (!shipment.receivedDate) {
        shipment.receivedDate = new Date();
      }
      
      showOrHidePpidAndExtIds();
    }

    function getItemAttrs() {
      if (shipment.isSpecimenShipment()) {
        return {collName: 'shipmentSpmns', itemKey: 'specimen', newItem: function(i) { return new Specimen(i) }};
      } else {
        return {collName: 'shipmentContainers', itemKey: 'container', newItem: function(i) { return new Container(i) }};
      }
    }

    function showOrHidePpidAndExtIds() {
      if (!shipment.isSpecimenShipment()) {
        return;
      }

      var result = ShipmentUtil.hasPpidAndExtIds(shipment.shipmentSpmns);
      angular.extend($scope.ctx, result);
    }

    $scope.passThrough = function() {
      return true;
    }

    //
    // initSpmnOpts is used during shipment to allow users select 
    // specimens that are suitable for shipment. No such thing exists
    // during receive; therefore it is assigned to behave same way
    // as pass through.
    //
    $scope.initSpmnOpts = $scope.passThrough;

    $scope.receive = function() {
      var shipment = angular.copy($scope.shipment);
      shipment.status = "Received";
      shipment.$saveOrUpdate().then(
        function(resp) {
          $state.go('shipment-detail.overview', {shipmentId: resp.id});
        }
      );
    }
    
    $scope.applyFirstLocationToAll = function() {
      var attrs = getItemAttrs();
      var location = shipment[attrs.collName][0][attrs.itemKey].storageLocation;
      if (!location.name) {
        return;
      }

      angular.forEach(shipment[attrs.collName],
        function(item, idx) {
          if (idx == 0) {
            return;
          }

          item[attrs.itemKey].storageLocation = {name: location.name, mode: location.mode};
        }
      );
    }

    $scope.copyFirstQualityToAll = function() {
      var attrs = getItemAttrs();
      var quality = shipment[attrs.collName][0].receivedQuality;

      angular.forEach(shipment[attrs.collName],
        function(item) {
          item.receivedQuality = quality;
        }
      );
    }

    function strCmp(s1, s2) {
      s1 = (s1 || '').toUpperCase();
      s2 = (s2 || '').toUpperCase();
      return s1 < s2 ? -1 : (s1 > s2 ? 1 : 0);
    }

    $scope.sortBy = function(attr) {
      ctx.direction = (attr == ctx.orderBy) ? (ctx.direction == 'asc' ? 'desc' : 'asc') : 'asc'
      ctx.orderBy = attr;

      var cmpFn;
      if (ctx.orderBy == 'label') {
        cmpFn = function(ss1, ss2) {
          return strCmp(ss1.specimen.label, ss2.specimen.label);
        }
      } else if (ctx.orderBy == 'externalId') {
        cmpFn = function(ss1, ss2) {
          return strCmp(
            $filter('osNameValueText')(ss1.specimen.externalIds),
            $filter('osNameValueText')(ss2.specimen.externalIds)
          );
        }
      } else if (ctx.orderBy == 'cp') {
        cmpFn = function(ss1, ss2) {
          return strCmp(ss1.specimen.cpShortTitle, ss2.specimen.cpShortTitle);
        }
      } else if (ctx.orderBy == 'ppid') {
        cmpFn = function(ss1, ss2) {
          return strCmp(ss1.specimen.ppid, ss2.specimen.ppid);
        }
      } else {
        cmpFn = function(ss1, ss2) {
          return ss1.specimen.id - ss2.specimen.id;
        }
      }

      shipment.shipmentSpmns.sort(
        function(ss1, ss2) {
          var factor = ctx.direction == 'desc' ? -1 : 1;
          return factor * cmpFn(ss1, ss2);
        }
      );
    }

    init();
  });
