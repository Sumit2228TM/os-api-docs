
angular.module('os.biospecimen.specimenkit')
  .controller('SpecimenKitListCtrl', function($scope, $state, $stateParams, SpecimenKit) {

    function init() {
      loadSpecimenKits();
    }

    function loadSpecimenKits() {
      SpecimenKit.list({cpId: $stateParams.cpId}).then(
        function(kits) {
          $scope.kits = kits;
        }
      );
    }

    $scope.showKitOverview = function(kit) {
      $state.go('specimen-kit-detail.overview', {kitId: kit.id});
    }

    init();
  });
