angular.module('os.administrative.containertype.addedit', ['os.administrative.models'])
  .controller('ContainerTypeAddEditCtrl', function(
    $scope, $state, containerType, ContainerType, PvManager, Util) {
    
    var defTypes = undefined;

    function init() {
      $scope.containerType = containerType;
      $scope.containerTypes = [];
      $scope.positionLabelingSchemes = PvManager.getPvs('container-position-labeling-schemes');
      $scope.positionAssignments = PvManager.getPvs('container-position-assignments');
    }

    function loadTypes(searchTerm) {
      if (defTypes && (!searchTerm || defTypes.length <= 100)) {
        $scope.containerTypes = defTypes;
        return;
      }

      ContainerType.query({name: searchTerm, maxResults: 101}).then(
        function(types) {
          $scope.containerTypes = types;
          if (!searchTerm) {
            defTypes = types;
          }
        }
      );
    }

    $scope.onStoreSpecimenEnabled = function() {
      $scope.containerType.canHold = undefined;
    };

    $scope.searchTypes = loadTypes;

    $scope.save = function() {
      var containerType = angular.copy($scope.containerType);
      containerType.$saveOrUpdate().then(
        function(result) {
          $state.go('container-type-detail.overview', {containerTypeId: result.id});
        }
      );
    };

    init();
  });
