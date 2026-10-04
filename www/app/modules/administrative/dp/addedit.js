
angular.module('os.administrative.dp.addedit', ['os.administrative.models', 'os.query.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, $translate, $q, currentUser, distributionProtocol, extensionCtxt,
    Alerts, DistributionProtocol, User, SavedQuery, ExtensionsUtil, Form, Alerts) {
    
    var defForms = undefined;
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.op = !distributionProtocol.id ? 'Create' : 'Update';
      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(distributionProtocol, extensionCtxt);
      $scope.userFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.queryList = [];
      $scope.formsList = [];
      $scope.all_sites = $translate.instant('dp.all_sites');
      loadDistInstSites();
      setDefaultDistInst();

      $scope.attachmentTypes = []
      $translate('common.none').then(
        function() {
          $scope.attachmentTypes = [
            {name: 'NONE',       caption: $translate.instant('dp.attachment_types.NONE')},
            {name: 'CSV_REPORT', caption: $translate.instant('dp.attachment_types.CSV_REPORT')},
            {name: 'MANIFEST',   caption: $translate.instant('dp.attachment_types.MANIFEST')},
            {name: 'BOTH',       caption: $translate.instant('dp.attachment_types.BOTH')}
          ]
        }
      );
    }

    function loadQueries(searchTerm) {
      $scope.queryList = SavedQuery.list({searchString: searchTerm});
    }

    function loadDistInstSites() {
      var dp = $scope.distributionProtocol;
      setDistributingSites(dp);
    }

    function setDistributingSites(dp) {
      var result = [];
      angular.forEach(dp.distributingSites, function (instituteSites, institute) {
        if (!instituteSites || instituteSites.length == 0) {
          result.push({instituteName: institute, sites: [$scope.all_sites]});
        } else {
          result.push({instituteName: institute, sites: instituteSites});
        }
      });
      
      if (result.length == 0) {
        result = [{instituteName: '', sites: []}];
      }
      
      dp.distributingSites = result;
    }
    
    function setDefaultDistInst() {
      if (currentUser.admin || !!distributionProtocol.id) {
        return;
      }

      distributionProtocol.distributingSites = [{
        instituteName: $scope.currentUser.instituteName,
        sites: []
      }];

      $scope.onDistInstSelect(0, $scope.currentUser.instituteName);
    }
    
    function newDistSite() {
      var dp = $scope.distributionProtocol;
      if (!dp.distributingSites) {
        dp.distributingSites = [];
      }
      
      dp.distributingSites.push({instituteName: '', sites: []});
    }
    
    function updateDistSites(dp) {
      var sites = {};
      angular.forEach(dp.distributingSites, function (distSite) {
        if (distSite.sites.indexOf($scope.all_sites) > -1) {
          sites[distSite.instituteName] = [];
        } else {
          sites[distSite.instituteName] = distSite.sites;
        }
      });
      
      dp.distributingSites = sites;
    }
    
    function isAllSitesPresent(index) {
      var dp = $scope.distributionProtocol;
      var sites = dp.distributingSites[index].sites;
      return sites.indexOf($scope.all_sites) > -1;
    }

    //
    // ng-required and therefore form validation is not working on
    // multi-select dropdown widgets; therefore we need to ensure at
    // least one site is selected when a non admin user is creating DP
    //
    function validateDistSites(dp) {
      if (currentUser.admin || currentUser.instituteAdmin) {
        return true;
      }

      if (!dp.distributingSites[0].sites || dp.distributingSites[0].sites.length == 0) {
        Alerts.error('dp.select_dist_site');
        return false;
      }

      return true;
    }

    function loadForms(searchTerm) {
      if (defForms && defForms.length < 100) {
        $scope.formsList = defForms;
        return;
      }

      if (!searchTerm && defForms) {
        $scope.formsList = defForms;
        return;
      }

      Form.query({name: searchTerm, maxResults: 100}).then(
        function(result) {
          $scope.formsList = result;
          if (!searchTerm) {
            defForms = result;
          }
        }
      );
    }
    
    $scope.createDp = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      if (!validateDistSites($scope.distributionProtocol)) {
        return;
      }

      var dp = angular.copy($scope.distributionProtocol);
      updateDistSites(dp);
      if (formCtrl) {
        dp.extensionDetail = formCtrl.getFormData();
      }

      dp.$saveOrUpdate().then(
        function(savedDp) {
          $state.go('dp-detail.overview', {dpId: savedDp.id});
        }
      );
    };

    $scope.onInstituteSelect = function(institute) {
      $scope.distributionProtocol.principalInvestigator = undefined;
      $scope.distributionProtocol.coordinators = undefined;
      $scope.userFilterOpts = {institute: institute};
      $scope.distributionProtocol.defReceivingSiteName = undefined;
    }
    
    $scope.onDistInstSelect = function(index, institute) {
      var dp = $scope.distributionProtocol;
      var sameInstituteIdx = dp.distributingSites.findIndex(
        function(instSite) {
          return instSite.instituteName == institute;
        }
      );

      if (sameInstituteIdx == index) {
        dp.distributingSites[index].sites = [];
      } else {
        dp.distributingSites[index].instituteName = '';
        Alerts.error('dp.dist_inst_pre_selected', {institute: institute, rowNo: sameInstituteIdx + 1});
      }
    }
    
    $scope.onDistSiteSelect = function(index) {
      var dp = $scope.distributionProtocol;
      var institute = dp.distributingSites[index].instituteName;
      if (isAllSitesPresent(index)) {
        dp.distributingSites[index].sites = [$scope.all_sites];
      }
    }
    
    $scope.removeDistSite = function(index) {
      var dp = $scope.distributionProtocol;
      dp.distributingSites.splice(index, 1);
      if (dp.distributingSites.length == 0) {
        newDistSite();
      }
    }
    
    $scope.addDistSite = newDistSite;

    $scope.loadQueries = loadQueries;

    $scope.searchForms = loadForms;

    init();
  });

