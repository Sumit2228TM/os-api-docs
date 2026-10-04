
angular.module('os.administrative.container')
  .controller('AddEditContainerSchedActivity', function(
    $scope, $modalInstance, activity, ContainerTask, PvManager) {

    var sctx;

    function init() {
      sctx = $scope.sctx = {
        intervalUnits: PvManager.getPvs('interval-units'),
        activity: activity
      };
    }

    $scope.searchTask = function(searchTerm) {
      if (sctx.defTasks && sctx.defTasks.length < 100) {
        sctx.tasks = sctx.defTasks;
      } else if (!searchTerm && sctx.defTasks) {
        sctx.tasks = sctx.defTasks;
      } else {
        ContainerTask.query({name: searchTerm}).then(
          function(tasks) {
            if (!searchTerm) {
              sctx.defTasks = tasks;
            }

            sctx.tasks = tasks;
          }
        );
      }
    }

    $scope.saveOrUpdate = function() {
      sctx.activity.taskId   = sctx.activity.$$uiProps.task.id;
      sctx.activity.taskName = sctx.activity.$$uiProps.task.name;
      sctx.activity.$saveOrUpdate().then(
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
