angular.module('openspecimen')
  .directive('osKeyValues', function() {
    return {
      restrict: 'C',

      link: function(scope, element, attrs) {
        scope.$watch(
          function() {
            var result = '';
            angular.forEach(element.find('li.item .value'),
              function(el, idx) {
                result += idx + el.scrollWidth;
              }
            );
            return result;
          },
          function() {
            angular.forEach(element.find('li.item .value'),
              function(el) {
                if (el.offsetWidth < el.scrollWidth) {
                  el.setAttribute('title', el.textContent.trim());
                }
              }
            );
          }
        );
      }
    };
  })

  .directive('osTooltip', function($compile) {
    return {
      restrict: 'A',

      terminal: true,

      priority: 100000,

      compile: function(tElem, tAttrs) {
        var tooltip = tAttrs.osTooltip;
        tElem.removeAttr('os-tooltip');
        tElem.attr({
          'bs-tooltip': '',
          'data-title': tooltip,
          'trigger'   : tAttrs.trigger || 'hover focus',
          'placement' : tAttrs.placement || 'auto',
          'container' : 'body',
          'html'      : (tAttrs.html == true || tAttrs.html == 'true')
        });

        var linkFn = $compile(tElem);
        return {
          pre: function(scope, element, attrs) {
          },

          post: function(scope, element, attrs) {
            linkFn(scope, function(clone) { element.replaceWith(clone); });
          }
        }
      }
    }
  });
