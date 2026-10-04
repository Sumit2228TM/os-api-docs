
angular.module('os.common.notif')
  .controller('NotifAlertCtrl', function($scope, $interval, UserNotification) {

    var ctx = {
      notifsOpen: false, 
      unreadCount: 0,
      openTime: undefined,
      stop: undefined
    };

    function init() {
      $scope.ctx = ctx;
      syncUnreadCount();

      $scope.$watch('ctx.notifsOpen',
        function(opened) {
          if (opened === true) {
            if (ctx.unreadCount > 0) {
              ctx.openTime = new Date();
            }
          } else if (opened === false && !!ctx.openTime) {
            UserNotification.markAsRead(ctx.openTime).then(
              function() {
                ctx.unreadCount = 0;
              }
            );
          }
        }
      );

      $scope.$on('$destroy',
        function() {
          if (ctx.stop) {
            $interval.cancel(ctx.stop);
          }
        }
      );
    }

    function syncUnreadCount() {
      if (ctx.notifsOpen) {
        scheduleNextSync();
        return;
      }

      UserNotification.getUnreadCount().then(
        function(count) {
          ctx.unreadCount = count;
          scheduleNextSync();
        },

        function(resp) {
          if (resp.status != 401) {
            scheduleNextSync();
          }
        }
      );
    }

    function scheduleNextSync() {
      ctx.stop = $interval(syncUnreadCount, 300000, 1);
    }

    init();
  });
