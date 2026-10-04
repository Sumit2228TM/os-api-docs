angular.module('os.administrative.containertask')
  .controller('ContainerTaskAddEditCtrl', function($scope, $state, task) {
    
    function init() {
      $scope.task = task;
    }

    $scope.save = function() {
      task.$saveOrUpdate().then(
        function(result) {
          $state.go('container-task-list');
        }
      );
    };

    init();
  });
