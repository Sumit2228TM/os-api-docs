
angular.module('os.common.notif')
  .controller('AllNotifsListCtrl', function($scope, $state, $stateParams, $window, notifs) {
    function init() {
      var pageNo = +($stateParams.pageNo || 0);

      var moreNotifs = (notifs.length == 11);
      if (moreNotifs) {
        notifs = notifs.slice(0, notifs.length - 1);
      }
          
      $scope.ctx = {
        notifs: notifs,
        currPage: pageNo + 1,
        totalNotifs: (pageNo + 1) * 1 + (moreNotifs ? 1 : 0),
        notifsPerPage: 1
      };

      $scope.$watch('ctx.currPage',
        function(newPageNo, oldPageNo) {
          if (newPageNo == oldPageNo) {
            return;
          }

          $state.go('notifications', {pageNo: newPageNo - 1});
        }
      );
    }

    $scope.visitNotifLink = function(notif) {
      if (!notif.href) {
        return;
      }
      $window.open(notif.href, notif.newTab ? '_blank' : '_self');
    }

    init();
  });
