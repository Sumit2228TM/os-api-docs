angular.module('os.administrative.dp.requirement.addedit', ['os.administrative.models'])
  .controller('DprAddEditCtrl', function($scope, $state, distributionProtocol, dpr, extensionCtxt, ExtensionsUtil) {

    function init() {
      $scope.lctx = {type: dpr.specimenType};
      $scope.dpr = dpr;
      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(dpr, extensionCtxt);
    }

    $scope.cancel = function() {
      $state.go('req-list');
    }

    $scope.onTypeSelect = function(type) {
      dpr.specimenType = type.type;
    }
    
    $scope.save = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var dpr = angular.extend(angular.copy($scope.dpr), {dp: {id: distributionProtocol.id}});
      if (formCtrl) {
        dpr.extensionDetail = formCtrl.getFormData();
      }

      dpr.$saveOrUpdate().then(
        function(saveReq) {
          $state.go('req-list');
        }
      );
    }
    
    init();
    
  });
