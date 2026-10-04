angular.module('os.administrative.dp')
  .controller('HomeDpListCtrl', function($scope, DistributionProtocol) {
    var ctx;

    function init(opts) {
      ctx = $scope.ctx = {
        defList: undefined,
        dps: []
      };

      $scope.$watch('opts.searchTerm', function(newVal) { loadDps(newVal); });
    }

    function loadDps(searchTerm) {
      if (!searchTerm && ctx.defList) {
        ctx.dps = ctx.defList;
        return;
      }

      DistributionProtocol.query({includeStats: false, query: searchTerm, orderByStarred: true, maxResults: 25}).then(
        function(dps) {
          ctx.dps = dps;
          if (!searchTerm) {
            ctx.defList = dps;
          }
        }
      );
    }

    $scope.init = init;

    $scope.toggleStar = function(dp) {
      var q = dp.starred ? dp.unstar() : dp.star();
      q.then(
        function(result) {
          if (result.status == true) {
            dp.starred = !dp.starred;
          }
        }
      );
    }
  });
