
angular.module('openspecimen')
  .directive('osUnsafeHtml', function($sce, $parse) {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        var value = $parse(attrs.osUnsafeHtml)(scope);
        if (value == null || value == undefined) {
          return;
        }

        if (!ui.os.appProps.allowHtmlMarkup) {
          element.text(value);
        } else {
          element.append($sce.getTrustedHtml($sce.trustAsHtml(value)));
        }
      }
    }
  });
