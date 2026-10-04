angular.module('os.biospecimen.consentstatement')
  .controller('ConsentStatementAddEditCtrl', function($scope, $state, consentStatement) {

    function init() {
      $scope.consentStatement = consentStatement; 
    }

    $scope.save = function() {
      $scope.consentStatement.$saveOrUpdate().then(
        function() {
          $state.go('consent-statement-list');
        }
      );
    }

    init();
  });
