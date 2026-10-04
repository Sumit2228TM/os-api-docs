angular.module('os.common.audit')
  .factory('AuditRevisionsModal', function($modal, Audit) {

    function show(objectsList) {
      $modal.open({
        templateUrl: 'modules/common/audit/revisions.html',
        controller: function($scope, $modalInstance, revisions) {
          $scope.revisions = revisions;

          $scope.done = function() {
            $modalInstance.close(true);
          }
        },
        resolve: {
          revisions: function() {
            return Audit.getRevisions(objectsList);
          }
        }
      });
    }

    return {
      show: show
    }
  });

