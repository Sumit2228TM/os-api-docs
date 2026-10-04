angular.module('os.biospecimen.specimenkit')
  .controller('SpecimenKitDetailCtrl', function($scope, specimenKit, Util) {
    $scope.kit = specimenKit;

    $scope.downloadReport = function() {
      var filename = specimenKit.cpShortTitle + '_kit_' + specimenKit.id;
      Util.downloadReport(specimenKit, 'specimen_kit', filename);
    }
  });
