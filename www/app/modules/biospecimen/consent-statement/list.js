angular.module('os.biospecimen.consentstatement')
  .controller('ConsentStatementListCtrl', function($scope, $state, ConsentStatement, Util) {

    var ctx;

    function init() {
      ctx = $scope.ctx = {
        statements: [],
        emptyState: {
          loading: true,
          empty: true,
          loadingMessage: 'consent_statement.loading_list',
          emptyMessage: 'consent_statement.empty_list'
        },
        filterOpts: Util.filterOpts({})
      };

      loadStmts(ctx.filterOpts);
      Util.filter($scope, 'ctx.filterOpts', loadStmts);
    }

    function loadStmts(filterOpts) {
      ctx.emptyState.loading = true;
      ConsentStatement.query(filterOpts).then(
        function(statements) {
          ctx.statements = statements;
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = (ctx.statements.length <= 0);
        }
      );
    }

    $scope.showStatementEdit = function(stmt) {
      $state.go('consent-statement-addedit', {stmtId: stmt.id});
    };

    init();
  });

