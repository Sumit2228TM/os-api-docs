
angular.module('os.common.notif')
  .directive('osNotifList',
    function($window, $document, UrlResolver, UserNotification) {

      function height(element, scrollTop) {
        return $document.height() - element.offset().top - scrollTop;
      }

      function setHeight(element, attrs) {
        element.children().css('max-height', height(element, attrs.scrollTop || 0)).css('overflow-y', 'auto');
      }

      return {
        restrict: 'E',

        templateUrl: 'modules/common/notif/list.html',

        link: function($scope, element, attrs) {
          $scope.ctx = { loading: true, notifs: [] };

          setHeight(element, attrs);
          angular.element($window).on('resize',
            function() {
              setHeight(element, attrs);
            }
          );

          UserNotification.query({startAt: 0, maxResults: 10}).then(
            function(notifs) {
              $scope.ctx.notifs = notifs;
              $scope.ctx.loading = false;
            }
          );

          $scope.visitNotifLink = function(notif) {
            if (!notif.href) {
              return;
            }
            $window.open(notif.href, notif.newTab ? '_blank' : '_self');
          }
        }
      }
    }
  );
