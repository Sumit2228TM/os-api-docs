angular.module('openspecimen')
  .directive('osRightDrawer', function(osRightDrawerSvc) {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        element.addClass('os-right-drawer').removeAttr('os-right-drawer');
        osRightDrawerSvc.setDrawer(element);

        scope.$on('$destroy', function() { osRightDrawerSvc.reset(); });
      }
    };
  })
  .directive('osRightDrawerToggle', function(osRightDrawerSvc) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        element.on('click', function() {
          osRightDrawerSvc.toggle();
        });
      }
    };
  })
  .factory('osRightDrawerSvc', function() {
    var drawerEl = undefined;

    var currentState;
    
    function setCardsViewWidth(width) {
      var cardsDiv = drawerEl.parent().find("div.container, div.os-cards, div.os-list-container");
      cardsDiv.css('width', width);
    }

    function open() {
      currentState = 'open';
      if (!drawerEl || drawerEl.hasClass('active')) {
        return;
      }

      var openWidth = +(drawerEl.attr('open-width') || 25);
      drawerEl.addClass('active');
      drawerEl.find('input, textArea, select, button').filter(':visible:first').focus();
      setCardsViewWidth((100 - openWidth) + '%') ;
      drawerEl.css('width', openWidth + '%');
      drawerEl.scope().$emit('osRightDrawerOpen');
    }

    function close() {
      currentState = 'close';
      if (!drawerEl || !drawerEl.hasClass('active')) {
        return;
      }

      drawerEl.removeClass('active');
      setCardsViewWidth('100%');
      drawerEl.scope().$emit('osRightDrawerClose');
    }
       
    return {
      setDrawer: function(drawer) {
        drawerEl = drawer;
        if (currentState == 'open') {
          open();
        } else if (currentState == 'close') {
          close();
        }
      },

      toggle: function() {
        drawerEl.hasClass('active') ? close() : open();
      },

      open: open,

      close: close,

      reset: function() {
        currentState = drawerEl = undefined;
      }
    }
  });
