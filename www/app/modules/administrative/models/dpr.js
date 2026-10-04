
angular.module('os.administrative.models.dpr', ['os.common.models'])
  .factory('DistributionProtocolRequirement', function(osModel) {
    var Dpr = osModel('distribution-protocol-requirements');
    
    Dpr.prototype.getType = function() {
      return 'distribution_protocol_requirement';
    }

    return Dpr;
  });

