
angular.module('os.administrative.models')
  .factory('ContainerTask', function(osModel) {
    var ContainerTask = new osModel('container-tasks');

    return ContainerTask;
  });
