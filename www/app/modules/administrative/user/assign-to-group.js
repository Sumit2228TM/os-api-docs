
angular.module('os.administrative.user')
  .directive('osAssignToUserGroup', function(UserGroup, $rootScope, Util) {

    function filterGroups(scope) {
      return function(filterOpts) { loadUserGroups(scope, filterOpts); };
    }

    function loadUserGroups(scope, filterOpts) {
      UserGroup.query(filterOpts).then(
        function(groups) {
          scope.groups = groups;
        }
      );
    }

    return {
      restrict: 'E',

      replace: true,

      scope: {
        onAddToGroup: '&'
      },

      templateUrl: 'modules/administrative/user/assign-to-group.html',

      link: function(scope, element, attrs) {
        scope.leftAlign = (attrs.menuAlign == 'left');
        scope.groupFilters = {};
        loadUserGroups(scope, scope.groupFilters);

        scope.addToGroup = function(group) {
          scope.onAddToGroup({group: group});
        }

        Util.filter(scope, 'groupFilters', filterGroups(scope));
      }
    }
  });
