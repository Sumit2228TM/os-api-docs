
angular.module('openspecimen')
  .filter('osBoolValue', function($translate) {
    return function(input, trueValue, falseValue, notSpecified) {
      var key = 'common.not_specified';

      if (input == true || input == 'true' || input == 1 || input == '1') {
        key = trueValue;
      } else if (input == false || input == 'false' || input == 0 || input == '0' || !notSpecified) {
        key = falseValue;
      } else {
        key = notSpecified;
      }

      return $translate.instant(key);
    }
  });
