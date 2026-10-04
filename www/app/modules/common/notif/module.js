
angular.module('os.common.notif', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('notifications', {
        url: '/notifications?pageNo',
        templateUrl: 'modules/common/notif/all.html',
        controller: 'AllNotifsListCtrl',
        resolve: {
          notifs: function($stateParams, UserNotification) {
            return UserNotification.query({startAt: +($stateParams.pageNo || 0) * 10, maxResults: 11});
          }
        },
        parent: 'signed-in'
      });
  });
