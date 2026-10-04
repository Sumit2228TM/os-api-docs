angular.module('os.biospecimen.cpgroups')
  .controller('CpGroupImportWorkflowsCtrl',
    function($scope, $state, $sce, group, CollectionProtocolGroup, CpConfigSvc, Alerts) {

      var ictx;

      function init() {
        ictx = $scope.ictx = {
          importer: {},
          importUrl: $sce.trustAsResourceUrl(CollectionProtocolGroup.url() + group.id + '/workflows-file')
        };
      }

      $scope.import = function(event) {
        event.preventDefault();
        ictx.importer.submit().then(
          function(wfDetail) {
            angular.forEach(group.cps,
              function(cp) {
                CpConfigSvc.setWorkflows(cp, wfDetail);
              }
            );

            Alerts.success('cp_groups.workflows_imported');
            $state.go('cp-group-detail.overview', {groupId: group.id});
          }
        );
      }

      $scope.cancel = function(event) {
        event.preventDefault();
        $scope.back();
      }

      init();
    }
  );
