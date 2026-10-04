
angular.module('openspecimen')
  .filter('osNumberInScientificNotation', function($translate, Util) {
    return function(input, placeholder) {

      var result = placeholder || $translate.instant("common.not_specified");
      if (angular.isUndefined(input) || input === null) {
        return result;
      }

      return Util.getNumberInScientificNotation(input, 1000000);
    }
  })
  .filter('osPadding', function() {
    return function(input, digits) {
      var padding = new Array(digits || 1).fill('0').join('');
      return (padding + (input || '')).slice(-digits);
    }
  });
