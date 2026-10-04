
angular.module('os.biospecimen.common')
  .directive('osStickyTreeTop', function($window, $document, $timeout) {

    function getUpButton(ctx) {
      var buttonEl = angular.element('<button/>')
        .addClass('os-tree-scroll-up-btn')
        .css('display', 'none')
        .append(angular.element('<span/>').addClass('fa fa-arrow-circle-up'))

      buttonEl.on('click', function() {
        scrollToTop(ctx);
        buttonEl.css('display', 'none');
      });

      return buttonEl;
    }

    function scrollToTop(ctx) {
      ctx.pane.scrollTop(0);
      unstickFromTop(ctx);
      ctx.fixedToTop = false;
    }

    function setWidth(ctx) {
      if (!ctx.fixedToTop) {
        return;
      }

      ctx.element.css('width', ctx.element.parent().width());
    }

    function setHeight(ctx) {
      if (!ctx.fixedToTop) {
        return;
      }

      ctx.element.css('height', ctx.pane.height() - 5);
    }

    function stickToTop(ctx) {
      ctx.element.css({position: 'fixed', top: ctx.pane.offset().top});
      setHeight(ctx);
      setWidth(ctx);
    }

    function unstickFromTop(ctx) {
      ctx.element.css({position: '', top: '', height: '', width: ''});
    }

    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        var pane = $document.find('.os-specimen-tree-pane');
        if (!pane) {
          return;
        }

        var ctx = {pane: pane, element: element, fixedToTop: false};

        var upBtn = getUpButton(ctx);
        angular.element($document[0].body).append(upBtn);

        scope.$on('osToggleParticipantSpecimenTree',
          function() {
            $timeout(function() { setWidth(ctx); });
          }
        );

        scope.$on('$destroy', function() { scrollToTop(ctx); upBtn.remove(); });

        angular.element($window).resize(
          function() {
            setWidth(ctx);
            setHeight(ctx);
          }
        );

        pane.on('scroll',
          function(e) {
            if (ctx.fixedToTop || (ctx.element.offset().top - ctx.pane.offset().top) > 0) {
              return;
            }

            ctx.fixedToTop = true;
            upBtn.css('display', 'block');
            stickToTop(ctx);
          }
        );
      }
    }
  });
