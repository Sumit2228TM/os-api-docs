angular.module('os.administrative.site.addedit', ['os.administrative.models'])
  .controller('SiteAddEditCtrl', function(
    $scope, $state, site, extensionCtxt, currentUser, ExtensionsUtil) {

    function init() {
      $scope.site = site;
      if (!currentUser.admin && !site.id) {
        site.instituteName = currentUser.instituteName;
      }

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(site, extensionCtxt);
      $scope.coordFilterOpts = {institute: $scope.site.instituteName};
    }


    $scope.onInstituteSelect = function() {
      $scope.coordFilterOpts = {institute: $scope.site.instituteName};
    }

    $scope.save = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var site = angular.copy($scope.site);
      if (formCtrl) {
        site.extensionDetail = formCtrl.getFormData();
      }

      site.$saveOrUpdate().then(
        function(savedSite) {
          $state.go('site-detail.overview', {siteId: savedSite.id});
        }
      );
    }

    init();
  });
