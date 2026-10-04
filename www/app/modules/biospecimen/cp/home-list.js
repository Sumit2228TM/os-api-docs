
angular.module('os.biospecimen.cp')
  .controller('HomeCpListCtrl', function($scope, CollectionProtocol) {
    var ctx;

    function init(opts) {
      ctx = $scope.ctx = {
        defList: undefined,
        cps: []
      };

      $scope.$watch('opts.searchTerm', function(newVal) { loadProtocols(newVal); });
    }

    function loadProtocols(searchTerm) {
      if (!searchTerm && ctx.defList) {
        ctx.cps = ctx.defList;
        return;
      }

      CollectionProtocol.list({detailedList: false, query: searchTerm, orderByStarred: true, maxResults: 25}).then(
        function(cps) {
          ctx.cps = cps;
          if (!searchTerm) {
            ctx.defList = cps;
          }
        }
      );
    }

    $scope.init = init;

    $scope.toggleStar = function(cp) {
      var q = cp.starred ? cp.unstar() : cp.star();
      q.then(
        function(result) {
          if (result.status == true) {
            cp.starred = !cp.starred;
          }
        }
      );
    }
  });
