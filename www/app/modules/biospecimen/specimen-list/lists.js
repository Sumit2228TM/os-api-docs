angular.module('os.biospecimen.specimenlist')
  .controller('SpecimenListsCtrl', function($scope, $state, lists, SpecimenList, Util, pagerOpts) {

    var ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = angular.extend(pagerOpts, {listSizeGetter: getSpecimenListsCount});
      ctx = $scope.ctx = {
        filterOpts: Util.filterOpts({maxResults: pagerOpts.recordsPerPage + 1}),
        emptyState: {
          loading: true,
          empty: true,
          loadingMessage: 'specimen_list.loading_list',
          emptyMessage: 'specimen_list.empty_list'
        }
      };

      setList(lists);
      Util.filter($scope, 'ctx.filterOpts', loadLists);
    }

    function setList(list) {
      ctx.lists = list;
      ctx.emptyState.loading = false;
      ctx.emptyState.empty = (list.length <= 0);
      pagerOpts.refreshOpts(list);
    }

    function loadLists(filterOpts) {
      ctx.emptyState.loading = true;
      var params = angular.extend({includeStats: true, orderByStarred: true}, filterOpts);
      SpecimenList.query(params).then(
        function(lists) {
          setList(lists);
        }
      );
    }

    function getSpecimenListsCount() {
      return SpecimenList.getCount($scope.ctx.filterOpts);
    }

    $scope.viewList = function(list) {
      $state.go('specimen-list', {listId: list.id});
    }

    $scope.toggleStar = function(list) {
      var q = list.starred ? list.unstar() : list.star();
      q.then(
        function(result) {
          if (result.status == true) {
            list.starred = !list.starred;
          }
        }
      );
    }

    $scope.pageSizeChanged = function() {
      ctx.filterOpts.maxResults = $scope.pagerOpts.recordsPerPage + 1;
    }

    init();
  });
