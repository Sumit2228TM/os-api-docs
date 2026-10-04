
angular.module('os.administrative.models')
  .factory('TimeZone', function(osModel) {
    var Tz = osModel('time-zones');

    return Tz;
  });
