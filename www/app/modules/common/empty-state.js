
angular.module('openspecimen')
  .directive('osEmptyList', function() {
    return {
      restrict: 'E',

      scope: {
        state: '='
      },

      templateUrl: 'modules/common/empty-state.html',

      link: function() { }
    }
  });
