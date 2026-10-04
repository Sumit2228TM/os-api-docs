
angular.module('os.biospecimen.participant')
  .controller('RegSpecimensCtrl', function($scope, cpr, specimens, Specimen) {
    function init() {
      $scope.specimens = specimens;
    }

    $scope.reload = function() {
      return Specimen.listFor(cpr.id).then(
        function(specimens) {
          $scope.specimens = specimens;
          return specimens;
        }
      );
    }

    init();
  });
