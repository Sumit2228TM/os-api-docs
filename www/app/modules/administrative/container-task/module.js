
angular.module('os.administrative.containertask', ['ui.router'])
  .config(function($stateProvider) {
    $stateProvider
      .state('container-task-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) { },
        parent: 'signed-in'
      })
      .state('container-task-list', {
        url:'/container-tasks',
        templateUrl:'modules/administrative/container-task/list.html',
        controller: 'ContainerTaskListCtrl',
        parent: 'container-task-root'
      })
      .state('container-task-addedit', {
        url: '/container-task-addedit/:taskId',
        templateUrl: 'modules/administrative/container-task/addedit.html',
        resolve: {
          task: function($stateParams, ContainerTask) {
            if ($stateParams.taskId) {
              return ContainerTask.getById($stateParams.taskId);
            }

            return new ContainerTask();
          }
        },
        controller: 'ContainerTaskAddEditCtrl',
        parent: 'container-task-root'
      })
  });
