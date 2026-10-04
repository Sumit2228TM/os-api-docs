angular.module('os.administrative.container')
  .controller('ContainerSpecimensCtrl', function(
    $scope, $state, $stateParams, container, currentUser,
    Util, SpecimensHolder, Alerts) {

    var lctx;

    function init() {
      $scope.ctx.showTree  = !$stateParams.filters;
      $scope.ctx.viewState = 'container-detail.specimens';

      lctx = $scope.lctx = {
        pagerOpts: undefined,

        params: {
          listName: 'container-specimens-list-view',
          objectId: container.id
        },

        emptyState: {
          loadingMessage: 'specimens.loading_list',
          emptyMessage: 'specimens.empty_list'
        },

        listCtrl: undefined
      };
    }

    function createNewList(spmns) {
      SpecimensHolder.setSpecimens(spmns);
      $state.go('specimen-list-addedit', {listId: ''});
    }

    $scope.setListCtrl = function(listCtrl) {
      listCtrl.autoSearchOpen = false;

      lctx.listCtrl = listCtrl;
      lctx.showSearch = listCtrl.haveFilters;
      lctx.pagerOpts  = listCtrl.pagerOpts;
    }

    $scope.toggleSearch = function() {
      $scope.ctx.showTree = !$scope.ctx.showTree;
    }

    $scope.downloadReport = function() {
      Util.downloadReport(container, "container.specimens");
    }

    $scope.loadSpecimens = function() {
      lctx.listCtrl.loadList();
    };

    $scope.getSelectedSpecimens = function() {
      var selectedSpmns = lctx.listCtrl.getSelectedItems();
      if (!selectedSpmns || selectedSpmns.length == 0) {
        return [];
      }

      return selectedSpmns.map(
        function(spmn) {
          return {
            id: spmn.hidden.specimenId,
            cpId: spmn.hidden.cpId
          };
        }
      );
    }

    $scope.addSpecimensToList = function(list) {
      var items = lctx.listCtrl.getSelectedItems();
      if (!items || items.length == 0) {
        Alerts.error('container.specimens.no_specimens_for_specimen_list');
        return;
      }

      var spmns = items.map(function(item) { return {id: item.hidden.specimenId}; });
      if (!list) {
        createNewList(spmns);
      } else {
        list.addSpecimens(spmns).then(
          function() {
            var type = list.getListType(currentUser);
            Alerts.success('specimen_list.specimens_added_to_' + type, list);
          }
        );
      }
    }

    init();
  });
