
angular.module('os.administrative.form')
  .controller('FormPreviewCtrl', function($scope, $stateParams, formDef) {
    var ctx;

    function init() {
      ctx = $scope.ctx = {
        formDef: formDef,
        formOpts: {
          formId: $stateParams.formId,
          formDef: formDef
        }
      }
    }

    init();
  });
