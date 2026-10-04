
angular.module('os.administrative.models')
  .factory('ScheduledContainerActivity', function(osModel) {
    var ScheduledContainerActivity = new osModel('scheduled-container-activities');

    return ScheduledContainerActivity;
  });
