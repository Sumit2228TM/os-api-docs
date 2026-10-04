angular.module('os.administrative.user')
  .controller('UserGroupsListCtrl', function($scope, $state, pagerOpts, UserGroup, Util, DeleteUtil) {

    var ctx;

    function init() {
      ctx = $scope.ctx = {
        pagerOpts: angular.extend(pagerOpts, {listSizeGetter: getGroupsCount}),
        filterOpts: Util.filterOpts({listAll: false, maxResults: pagerOpts.recordsPerPage + 1}),
        emptyState: {
          loading: true,
          empty: true,
          loadingMessage: 'user.loading_groups',
          emptyMessage: 'user.no_groups'
        }
      };
      pagerOpts = ctx.pagerOpts;

      loadGroups(ctx.filterOpts);
      Util.filter($scope, 'ctx.filterOpts', loadGroups);
    }

    function setGroups(groups) {
      ctx.groups = groups;
      ctx.emptyState.loading = false;
      ctx.emptyState.empty = (groups.length <= 0);
      pagerOpts.refreshOpts(groups);
    }

    function loadGroups(filterOpts) {
      ctx.emptyState.loading = true;
      var params = angular.extend({includeStats: true}, filterOpts);
      UserGroup.query(params).then(
        function(groups) {
          setGroups(groups);
        }
      );
    }

    function getGroupsCount() {
      return UserGroup.getCount(ctx.filterOpts);
    }

    $scope.viewGroup = function(group) {
      $state.go('user-list', {groupId: group.id});
    }

    $scope.deleteGroup = function(group) {
      DeleteUtil.delete(group, {
        onDeletion: function() {
          loadGroups(ctx.filterOpts);
        },
        deleteWithoutCheck: true
      });
    }

    $scope.pageSizeChanged = function() {
      ctx.filterOpts.maxResults = $scope.pagerOpts.recordsPerPage + 1;
    }

    init();
  });
