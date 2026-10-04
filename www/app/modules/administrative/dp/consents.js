
angular.module('os.administrative.dp.consents', ['os.administrative.models'])
  .controller('DpConsentsCtrl',function(
    $scope, $http, $q, distributionProtocol, hasEc,
    currentUser, consentTiers, DeleteUtil, ConsentStatement) {

    var defStmts;

    function init() {
      $scope.consentCtx = {
        hasEc: hasEc,
        tiers: initConsentTiers(consentTiers),
        stmts: []
      };

      if (!hasEc) {
        loadConsentStmts();
      }
    }

    function initConsentTiers(consentTiers) {
      angular.forEach(consentTiers, initConsentTier);
      return consentTiers;
    }

    function initConsentTier(consentTier) {
      return addDisplayValue(consentTier, consentTier.statementCode, consentTier.statement);
    }

    function loadConsentStmts(searchString) {
      if (defStmts && !searchString) {
        $scope.consentCtx.stmts = defStmts;
        return;
      }

      if (defStmts && defStmts.length < 100) {
        return;
      }

      ConsentStatement.query({searchString: searchString}).then(
        function(stmts) {
          $scope.consentCtx.stmts = stmts;
          angular.forEach(stmts,
            function(stmt) {
              addDisplayValue(stmt, stmt.code, stmt.statement);
            }
          );

          if (!searchString) {
            defStmts = stmts;
          }
        }
      );
    }

    function addDisplayValue(obj, code, statement) {
      return angular.extend(obj, {itemKey: code, displayValue: statement + ' (' + code + ')'});
    }

    $scope.loadConsentStmts = loadConsentStmts;

    $scope.listChanged = function(action, tier) {
      if (action == 'add') {
        return distributionProtocol.newConsentTier({statementCode: tier.itemKey}).$saveOrUpdate().then(initConsentTier);
      } else if (action == 'remove') {
        var deferred = $q.defer();
        var opts = {
          deleteWithoutCheck: true,
          confirmDelete: 'dp.delete_consent_tier',
          onDeletion: function() { deferred.resolve(true); },
          onDeleteFail: function() { deferred.reject(); }
        }

        DeleteUtil.delete(tier, opts);
        return deferred.promise;
      } else if (action == 'update') {
        return distributionProtocol.newConsentTier({
          id: tier.id,
          statementCode: tier.displayValue
        }).$saveOrUpdate().then(initConsentTier);
      }

      return undefined;
    };

    init();
  });
