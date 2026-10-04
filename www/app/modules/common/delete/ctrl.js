angular.module('os.common.delete', [])
  .controller('EntityDeleteCtrl', function($scope, $modalInstance, entityProps, dependentEntities, Alerts) {

    function init() {
      dependentEntities = dependentEntities || [];

      $scope.submitted         = false;
      $scope.entity            = entityProps.entity;
      $scope.entityProps       = entityProps;
      $scope.dependentEntities = dependentEntities;
      $scope.showDependents    = !entityProps.forceDelete && dependentEntities.length > 0;
    }

    function onDeletion(entity) {
      if (!!entity) {
        if (entity.completed != false) {
          Alerts.success("delete_entity.entity_deleted", entityProps);
          $modalInstance.close(entity);
        } else {
          Alerts.info("delete_entity.delete_pending", entityProps);
          $modalInstance.close(entity);
        }
      }
    }

    function onBulkDeletion(entity) {
      if (!!entity) {
        if (entity.completed != false) {
          Alerts.success(entityProps.successMessage);
        } else {
          Alerts.info(entityProps.pendingMessage)
        }

        $modalInstance.close(entity);
      }
    }

    function bulkDelete() {
      $scope.entity.bulkDelete(entityProps.entityIds, entityProps.reason).then(onBulkDeletion, submitFailed)
    };

    function submitFailed() {
      $scope.submitted = false;
    }

    $scope.delete = function () {
      $scope.submitted = true;
      if (entityProps.entityIds) {
        bulkDelete();
      } else {
        $scope.entity.$remove(!!entityProps.forceDelete, entityProps.reason).then(onDeletion, submitFailed);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })


