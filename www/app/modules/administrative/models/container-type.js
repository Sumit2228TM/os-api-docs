
angular.module('os.administrative.models.containertype', ['os.common.models'])
  .factory('ContainerType', function(osModel, $http) {
    var ContainerType = new osModel('container-types');

    ContainerType.prototype.getType = function() {
      return 'container_type';
    }

    ContainerType.prototype.getDisplayName = function() {
      return this.name;
    }

    ContainerType.bulkDelete = function(typeIds) {
      return $http.delete(ContainerType.url(), {params: {id: typeIds, forceDelete: true}}).then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    return ContainerType;
  });
