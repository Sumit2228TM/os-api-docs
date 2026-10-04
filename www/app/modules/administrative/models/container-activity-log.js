
angular.module('os.administrative.models')
  .factory('ContainerActivityLog', function(osModel) {
    var ContainerActivityLog = new osModel('container-activity-logs');

    return ContainerActivityLog;
  });
