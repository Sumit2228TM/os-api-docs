
angular.module('os.query')
  .controller('QueryAuditLogsCtrl', function($scope, $modal, Util, QueryAuditLog) {
    var ctx = {
      pageOpts: {currPage: 1, totalLogs: 0, pageSize: 25},
      filterOpts: {},
      logs: [],
      loading: false
    };

    function init() {
      $scope.ctx = ctx;

      loadLogs();
      $scope.$watch('ctx.pageOpts.currPage', loadLogs);

      Util.filter($scope, 'ctx.filterOpts', 
        function() {
          ctx.pageOpts = {currPage: 1, totalLogs: 0, pageSize: 25}
          loadLogs();
        }
      );
    }

    function loadLogs() {
      var filterOpts = angular.extend({}, ctx.filterOpts || {});

      var pageOpts = {
        startAt: (ctx.pageOpts.currPage - 1) * ctx.pageOpts.pageSize,
        maxResults: ctx.pageOpts.pageSize + 1
      };
      angular.extend(filterOpts, pageOpts);
      
      ctx.loading = true;
      QueryAuditLog.list(filterOpts).then(
        function(logs) {
          ctx.pageOpts.totalLogs = (ctx.pageOpts.currPage - 1) * ctx.pageOpts.pageSize + logs.length;

          if (logs.length > ctx.pageOpts.pageSize) {
            logs.splice(logs.length - 1, 1);
          }

          ctx.logs = logs;
          ctx.loading = false;
        }
      );
    }

    $scope.viewSql = function(log) {
      $modal.open({
        templateUrl: 'modules/query/audit-log-sql.html',
        controller: function($scope, log, $modalInstance) {
          $scope.log = log;

          $scope.ok = function() {
            $modalInstance.close();
          }
        },
        resolve: {
          log: function() {
            return QueryAuditLog.getById(log.id);
          }
        },
        size: 'lg'
      });
    }

    init();
  });
