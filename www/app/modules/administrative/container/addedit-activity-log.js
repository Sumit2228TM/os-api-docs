angular.module('os.administrative.container')
  .controller('AddEditContainerActivityLogCtrl', function(
    $scope, $modalInstance, activityLog,
    ContainerTask, ScheduledContainerActivity) {

    var actx;

    function init() {
      actx = $scope.actx = {
        activityLog: activityLog
      }
    }

    $scope.searchTask = function(searchTerm) {
      if (actx.defTasks && actx.defTasks.length < 100) {
        actx.tasks = actx.defTasks;
      } else if (!searchTerm && actx.defTasks) {
        actx.tasks = actx.defTasks;
      } else {
        ContainerTask.query({name: searchTerm}).then(
          function(tasks) {
            if (!searchTerm) {
              actx.defTasks = tasks;
            }

            actx.tasks = tasks;
          }
        );
      }
    }

    $scope.searchActivities = function(searchTerm) {
      if (actx.defActivities && actx.defActivities.length < 100) {
        actx.activities = actx.defActivities;
      } else if (!searchTerm && actx.defActivities) {
        actx.activities = actx.defActivities;
      } else {
        ScheduledContainerActivity.query({containerId: activityLog.containerId, taskName: searchTerm}).then(
          function(activities) {
            if (!searchTerm) {
              actx.defActivities = activities;
            }

            actx.activities = activities;
          }
        );
      }
    }

    $scope.saveOrUpdate = function() {
      var toSave = angular.copy(actx.activityLog);
      if (toSave.$$uiProps.adHoc) {
        toSave.taskId = toSave.$$uiProps.task.id;
        toSave.scheduledActivityId = toSave.taskName = null;
      } else {
        toSave.scheduledActivityId = toSave.$$uiProps.activity.id;
        toSave.taskId = toSave.taskName = null;
      }

      toSave.$saveOrUpdate().then(
        function(savedActivity) {
          $modalInstance.close(savedActivity);
        }
      );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
