angular.module('openspecimen')
  .controller('AboutOSCtrl', function($scope, $modal) {

    $scope.showAboutOS = function() {
      $modal.open({
        templateUrl: 'modules/common/about.html',
        controller: function($scope) {
          $scope.props = ui.os.appProps;
        }
      });
    };
  });
