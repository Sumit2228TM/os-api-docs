
angular.module('os.biospecimen.specimen.close', ['os.biospecimen.models'])
  .controller('SpecimenCloseCtrl', function($scope, $modalInstance, Specimen, specimens, Alerts) {

    var cs = undefined;

    function init() {
      cs = $scope.closeSpec = {
        reason: '',
        date: new Date()
      };
    }

    function bulkClose() {
      var specs = (specimens || []).map(
        function(specimen) {
          return {
            id: specimen.id, status: 'Closed',
            reason: cs.reason, user: cs.user, date: cs.date
          };
        }
      );

      Specimen.bulkStatusUpdate(specs).then(
        function(result) {
          Alerts.success('specimens.specimens_closed');
          $modalInstance.close(result);
        }
      );
    }

    $scope.close = function() {
      bulkClose();
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
