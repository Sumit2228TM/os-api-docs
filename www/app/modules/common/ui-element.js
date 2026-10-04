angular.module('openspecimen')
  .directive('osUiElement', function($parse) {
    return {
      restrict: 'A',

      scope: true,

      link: function(scope, element, attrs) {
         if (attrs.osUiElement) {
           $parse(attrs.osUiElement).assign(scope, element);
         }
      }
    }
  });
