angular.module('os.administrative.container')
  .controller('ContainerEventsCtrl', function($scope, events) {

    var lctx;

    function init() {
      $scope.ctx.viewState = 'container-detail.events';
      lctx = $scope.lctx = {
        events: events.sort(function(e1, e2) { return e2.id - e1.id; }),
        emptyState: {
          empty: events.length <= 0,
          emptyMessage: 'container.no_transfer_events'
        }
      };
    }

    init();
  });
