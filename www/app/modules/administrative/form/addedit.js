angular.module('os.administrative.form.addedit', ['os.administrative.models'])
  .controller('FormAddEditCtrl', function($scope, $state, $sce, $timeout, form) {
    
    function init() {
      $scope.form = form;
      $scope.url = "form-designer/";
      
      if (form.id) {
         $scope.url += "?formId=" + form.id;
      }

      $scope.url = $sce.trustAsResourceUrl($scope.url);
      window.addEventListener('message', saveForm);

      $scope.$on('$destroy', function() {
        window.removeEventListener('message', saveForm);
      });
    }

    function saveForm(evt) {
      if (evt.origin != window.origin) {
        return;
      }

      if (evt.data == 'done') {
        $state.go('form-list');
        return;
      }

      $timeout(
        function() {
          $scope.form = evt.data;
        }
      );
    }

    init(); 
  });
