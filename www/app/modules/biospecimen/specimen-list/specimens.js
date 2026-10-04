angular.module('os.biospecimen.specimenlist')
  .controller('SpecimenListSpecimensCtrl', function(
    $scope, $state, $stateParams, $injector, currentUser, list,
    Specimen, SpecimensHolder, DeleteUtil, Alerts, Util, SettingUtil) {

    function init() { 
      $scope.specimenUpdateOpts = {resources: ['Specimen', 'PrimarySpecimen'], operations: ['Update']};
      $scope.orderCreateOpts    = {resource: 'Order', operations: ['Create']};

      $scope.ctx = {
        list: list,
        params: {
          listName: 'cart-specimens-list-view',
          objectId: list.id
        },
        breadcrumbs: $stateParams.breadcrumbs,
        reqBasedDistOrShip: false,
        emptyState: {
          loadingMessage: 'specimens.loading_list',
          emptyMessage: 'specimens.empty_list'
        }
      }

      if ($injector.has('spmnReqCfgUtil')) {
        $injector.get('spmnReqCfgUtil').isReqBasedDistOrShippingEnabled().then(
          function(result) {
            $scope.ctx.reqBasedDistOrShip = (result.value == 'true');
          }
        );
      }

      SettingUtil.getSetting('common', 'cart_specimens_rpt_query').then(
        function(setting) {
          $scope.ctx.rptTmplConfigured = !!setting.value;
        }
      );
    }

    function loadSpecimens() {
      $scope.ctx.listCtrl.loadList();
    };

    function removeSpecimensFromList() {
      var list = $scope.ctx.list;
      list.removeSpecimens($scope.getSelectedSpecimens()).then(
        function(listSpecimens) {
          var type = list.getListType(currentUser);
          Alerts.success('specimen_list.specimens_removed_from_' + type, list);
          loadSpecimens();
        }
      );
    }

    function gotoView(state, params, msgCode) {
      var selectedSpmns = $scope.ctx.listCtrl.getSelectedItems();
      if (!selectedSpmns || selectedSpmns.length == 0) {
        Alerts.error('specimen_list.' + msgCode);
        return;
      }

      var ids = selectedSpmns.map(function(spmn) { return spmn.hidden.specimenId; });
      Specimen.getByIds(ids).then(
        function(spmns) {
          SpecimensHolder.setSpecimens(spmns);
          $state.go(state, params);
        }
      );
    }

    $scope.loadSpecimens = loadSpecimens;

    $scope.getSelectedSpecimens = function() {
      var selectedSpmns = $scope.ctx.listCtrl.getSelectedItems();
      return (selectedSpmns || []).map(
        function(spmn) {
          return new Specimen({id: spmn.hidden.specimenId, cpId: spmn.hidden.cpId});
        }
      );
    }

    $scope.addChildSpecimens = function() {
      var list = $scope.ctx.list;
      list.addChildSpecimens().then(
        function() {
          Alerts.success('specimen_list.child_specimens_added');
          loadSpecimens();
        }
      );
    }

    $scope.viewSpecimen = function(specimen) {
      $state.go('specimen', {specimenId: specimen.id});
    }

    $scope.confirmRemoveSpecimens = function () {
      var selectedSpmns = $scope.ctx.listCtrl.getSelectedItems();
      if (!selectedSpmns || selectedSpmns.length == 0) {
        Alerts.error("specimen_list.no_specimens_for_deletion");
        return;
      }

      var listType = list.getListType(currentUser);
      DeleteUtil.confirmDelete({
        entity: list,
        props: {messageKey: 'specimen_list.confirm_remove_specimens_from_' + listType},
        templateUrl: 'modules/biospecimen/specimen-list/confirm-remove-specimens.html',
        delete: removeSpecimensFromList
      });
    }

    $scope.setListCtrl = function(listCtrl) {
      $scope.ctx.listCtrl = listCtrl;
      $scope.ctx.showSearch = listCtrl.haveFilters;
    }

    $scope.downloadReport = function() {
      var selectedSpmns = $scope.ctx.listCtrl.getSelectedItems();
      var params = {};
      if (selectedSpmns && selectedSpmns.length > 0) {
        params.specimenId = selectedSpmns.map(function(spmn) { return spmn.hidden.specimenId; });
      }

      Util.downloadReport(list, 'specimen_list', list.getDisplayName(), params);
    }

    init();
  });
