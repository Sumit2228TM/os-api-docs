
angular.module('os.administrative',
  [
    'os.administrative.models',
    'os.administrative.user',
    'os.administrative.institute',
    'os.administrative.site',
    'os.administrative.dp',
    'os.administrative.order',
    'os.administrative.shipment',
    'os.administrative.container',
    'os.administrative.containertype',
    'os.administrative.containertask',
    'os.administrative.role',
    'os.administrative.form',
    'os.administrative.job',
    'os.administrative.setting'
  ]
).config(function($stateProvider) {
    $stateProvider
      .state('ext-app-messages', {
        url: '/ext-app-messages/:msgId',
        templateUrl: 'modules/administrative/support/message-log.html',
        resolve: {
          messageLog: function($stateParams, $http, ApiUrls) {
            if ($stateParams.msgId > 0) {
              return $http.get(ApiUrls.getBaseUrl() + '/ext-app-messages/' + $stateParams.msgId).then(
                function(resp) {
                  return resp.data;
                }
              );
            }

            alert("No or invalid message ID: " + $stateParams.msgId);
            return {message: 'No or invalid message ID: ' + $stateParams.msgId};
          }
        },
        controller: function($scope, messageLog) {
          $scope.messageLog = messageLog;
          if (messageLog.message) {
            messageLog.message = messageLog.message
              .replaceAll("\r\n", "\n")
              .replaceAll("\n\r", "\n")
              .replaceAll("\r", "\n");
          }

          try {
            messageLog.message = JSON.parse(messageLog.message);
            messageLog.displayType = 'json';
          } catch (e) {
          }
        },
        parent: 'signed-in'
      });
});

