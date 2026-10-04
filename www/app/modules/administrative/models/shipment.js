
angular.module('os.administrative.models.shipment', ['os.common.models'])
  .factory('Shipment', function(osModel, $http) {
    var Shipment = osModel('shipments');

    Shipment.prototype.getType = function() {
      return 'shipment';
    }

    Shipment.prototype.getDisplayName = function() {
      return this.name;
    }

    Shipment.prototype.isSpecimenShipment = function() {
      return this.type == 'SPECIMEN';
    }

    Shipment.prototype.$saveProps = function() {
      this.request = !!this.request ? {id: this.request.id} : undefined;
      angular.forEach(this.shipmentItems, function(shipmentItem) {
        shipmentItem.specimen = {
          id: shipmentItem.specimen.id,
          storageLocation: shipmentItem.specimen.storageLocation
        };
      });

      return this;
    }
    
    Shipment.prototype.generateReport = function() {
      return $http.get(Shipment.url() + this.$id() + "/report").then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Shipment.prototype.getSpecimens = function(startAt, maxSpmns, orderBy, direction) {
      var params = {orderBy: orderBy, direction: direction, startAt: startAt, maxResults: maxSpmns};
      return $http.get(Shipment.url() + this.$id() + '/specimens', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Shipment.prototype.getContainers = function(startAt, maxSpmns, orderBy, direction) {
      var params = {orderBy: orderBy, direction: direction, startAt: startAt, maxResults: maxSpmns};
      return $http.get(Shipment.url() + this.$id() + '/containers', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Shipment.prototype.searchContainers = function(names) {
      var params = {sendingSite: this.sendingSite, receivingSite: this.receivingSite, name: names};
      return $http.get(Shipment.url() + '/containers', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return Shipment;
  }
);
