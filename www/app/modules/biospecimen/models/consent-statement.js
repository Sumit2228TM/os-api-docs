
angular.module('os.biospecimen.models.consentstatement', ['os.common.models'])
  .factory('ConsentStatement', function(osModel) {
    var ConsentStatement = new osModel('consent-statements');

    return ConsentStatement;
  });

