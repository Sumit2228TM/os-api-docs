angular.module('os.common.notif')
  .factory('UserNotification', function($http, osModel, UrlResolver) {
    var UserNotification = osModel(
      'user-notifications',
       function(notif) {
         if (!notif.urlKey) {
           return;
         }

         notif.href = notif.urlKey;
         notif.newTab = true;
         if (notif.urlKey.indexOf('http://') != 0 && notif.urlKey.indexOf('https://') != 0) {
           notif.href = UrlResolver.getUrl(notif.urlKey, notif.entityId);
           notif.newTab = false;
         }
       }
    );
 
    UserNotification.getUnreadCount = function() {
      return $http.get(UserNotification.url() + '/unread-count', {ignoreLoadingBar: true})
        .then(function(resp) { return resp.data.count; });
    }

    UserNotification.markAsRead = function(notifsBefore) {
      return $http.put(UserNotification.url() + '/mark-as-read', {notifsBefore: notifsBefore})
        .then(function(resp) { return resp.data; });
    }

    return UserNotification;
  });
