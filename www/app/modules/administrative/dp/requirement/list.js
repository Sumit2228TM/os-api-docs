angular.module('os.administrative.dp.requirement.list', ['os.administrative.models'])
  .controller('DprListCtrl', function($scope, requirements, PluginReg, DeleteUtil) {
  
    var ctx;
    function init() {
      ctx = $scope.ctx = {
        dprs: requirements,
        hdrTmpls: PluginReg.getTmpls('dp-req-detail', 'fields-header', ''),
        cellTmpls: PluginReg.getTmpls('dp-req-detail', 'fields-overview', ''),

        emptyState: {
          empty: requirements.length <= 0,
          emptyMessage: 'dp.empty_reqs_list'
        }
      }
    }
  
    $scope.deleteDpr = function(dpr) {
      DeleteUtil.confirmDelete({
        entity: dpr,
        templateUrl: 'modules/administrative/dp/requirement/delete.html',
        delete : function() {
          dpr.$remove().then(
            function() {
              var index = $scope.ctx.dprs.indexOf(dpr);
              ctx.dprs.splice(index, 1);
              ctx.emptyState.empty = ctx.dprs.length <= 0;
              $scope.input.targets.splice(index, 1);
            }
          );
        }
      });
      
    }
    
    init();
  });

