
angular.module('os.administrative.shipment')
  .factory('ShipmentUtil', function() {

    function hasPpidAndExtIds(items) {
      var hasPpid = false, hasExtId = false;
      for (var i = 0; i < items.length; ++i) {
        var item = items[i];

        if (!hasPpid && item.specimen.ppid.indexOf('$$cp_reg_') != 0) {
          hasPpid = true;
        }

        if (!hasExtId && item.specimen.externalIds && item.specimen.externalIds.length > 0) {
          hasExtId = true;
        }

        if (hasPpid && hasExtId) {
          break;
        }
      }

      return {hasPpid: hasPpid, hasExtId: hasExtId};
    }

    return {
      hasPpidAndExtIds: hasPpidAndExtIds
    }
  });
