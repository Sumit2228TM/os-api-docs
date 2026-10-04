angular.module('openspecimen')
  .directive('osHtmlTemplate', function($compile) {
    return {
      restrict: 'E',

      replace: true,

      template: '<span></span>',

      link: function(scope, element, attrs) {
        var template = scope.$eval(attrs.template);
        element.html(template);
        $compile(element.contents())(scope);
      }
    };
  });
