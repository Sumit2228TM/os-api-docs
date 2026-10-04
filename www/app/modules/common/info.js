angular.module('openspecimen')
  .directive('osInfo', function($modal) {
    return {
      restrict: 'E',
      replace: true,
      template:
        '<li class="info" dropdown>' +
          '<a class="dropdown-toggle" ng-click="showVersionInfo()">' +
            '<span class="fa fa-info-circle"></span>' +
          '</a>' +
        '</li>',
      link: function(scope, element, attrs) {
        scope.showVersionInfo = function() {
          $modal.open({
            templateUrl: 'modules/common/about.html',
            controller: function($scope) {
              $scope.props = ui.os.appProps;
            }
          });
        }
      }
    };
  });
