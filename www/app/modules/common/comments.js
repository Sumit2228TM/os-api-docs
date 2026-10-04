
angular.module('os.common')
  .controller('CommentsCtrl', function($scope, $modalInstance, ctx) {
    function init() {
      $scope.ctx = angular.extend({reason: undefined}, ctx);
    };

    $scope.done = function() {
      $modalInstance.close($scope.ctx.reason);
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  })

  .factory('CommentsUtil', function($modal) {
    return {
      getComments: function(ctx, callback) {
        return $modal.open({
          templateUrl: 'modules/common/comments.html',
          controller: 'CommentsCtrl',
          resolve: {
            ctx: function() {
              return ctx
            }
          }
        }).result.then(callback);
      }
    }
  });
