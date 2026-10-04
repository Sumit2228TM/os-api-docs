
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, $modal, $translate, currentUser, distributionProtocol, Site, DeleteUtil) {
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.permCtx = {editAllowed: false, deleteAllowed: false};

      var allowed = areOpsAllowed(['Update', 'Delete'])
      if (allowed === true || allowed === false) {
        $scope.permCtx.editAllowed = $scope.permCtx.deleteAllowed = allowed;
      } else {
        $q.all(allowed).then(
          function(result) {
            $scope.permCtx.editAllowed   = result[0];
            $scope.permCtx.deleteAllowed = result[1];
          }
        );
      }
    }

    function areOpsAllowed(ops) {
      if (currentUser.admin) {
        //
        // admin is allowed to perform all ops
        //
        return true;
      }

      var institutes = [], sites = [];
      angular.forEach(distributionProtocol.distributingSites,
        function(instSites, institute) {
          institutes.push(institute);
          if (instSites && instSites.length > 0) {
            sites = sites.concat(instSites);
          }
        }
      );

      if (institutes.length > 1) {
        //
        // only an admin can perform ops on distribution protocol involving
        // multiple institutes
        //
        return false;
      }

      var dpSites;
      if (sites && sites.length > 0) {
        var q = $q.defer();
        q.resolve(sites);
        dpSites = q.promise;
      } else {
        dpSites = Site.list({institute: institutes[0], listAll: true});
      }

      return ops.map(
        function(op) {
          var allowedSites = Site.list({resource: 'DistributionProtocol', operation: op});
          return $q.all([dpSites, allowedSites]).then(
            function(result) {
              return result[0].every(function(site) { return result[1].indexOf(site) >= 0; });
            }
          );
        }
      );
    }

    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      DeleteUtil.delete($scope.distributionProtocol, {onDeleteState: 'dp-list'});
    }
    
    $scope.closeDp = function () {
      DeleteUtil.confirmDelete({
        entity: distributionProtocol,
        templateUrl: 'modules/administrative/dp/close.html',
        delete: function () {
          distributionProtocol.close().then(function(dp) {
            $scope.distributionProtocol = dp;
          })
        }
      });
    }
    
    $scope.reopenDp = function () {
      distributionProtocol.reopen().then(function (dp) {
        $scope.distributionProtocol = dp;
      });
    }

    init();
  });
