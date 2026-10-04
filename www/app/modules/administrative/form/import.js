
angular.module('os.administrative.form')
  .controller('FormImportCtrl', function($scope, $state, $sce, Form) {
    $scope.formImporter = {};
    $scope.formImportUrl = $sce.trustAsResourceUrl(Form.url() + 'definition-zip');
    

    $scope.importForm = function(event) {
      event.preventDefault();
      $scope.formImporter.submit().then(
        function(importedForm) {
          $state.go('form-addedit', {formId: importedForm.formId});
        }
      );
    }

    $scope.cancel = function(event) {
      event.preventDefault();
      $scope.back();
    }
  });
