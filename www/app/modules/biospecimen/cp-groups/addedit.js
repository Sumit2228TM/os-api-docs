
angular.module('os.biospecimen.cpgroups')
  .controller('CpGroupAddEditCtrl', function($scope, $state, group, CollectionProtocol, CollectionProtocolGroup, Alerts) {

    var defaultCps = undefined;

    function init() {
      $scope.group = group;
      $scope.cps = [];
    }

    function loadCps(searchTerm) {
      if (defaultCps && defaultCps.length < 100) {
        $scope.cps = defaultCps;
        return;
      }

      CollectionProtocol.query({title: searchTerm, op: 'Update', resource: 'CollectionProtocol'}).then(
        function(cps) {
          if (!searchTerm) {
            defaultCps = cps;
          }

          $scope.cps = cps;
        }
      );
    }

    $scope.searchCps = loadCps;

    $scope.saveOrUpdate = function() {
      if (!group.cps || group.cps.length <= 0) {
        Alerts.error('cp_groups.no_cp_specified');
        return;
      }

      group.$saveOrUpdate().then(
        function(savedGroup) {
          $state.go('cp-group-detail.overview', {groupId: savedGroup.id});
        }
      );
    }

    init();
  });
