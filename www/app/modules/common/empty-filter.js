
angular.module('openspecimen')
  .filter('osNoValue', function($translate, SettingUtil) {

    function notSpecifiedText(placeholder) {
      if (!!placeholder) {
        return placeholder;
      }


      var result = SettingUtil.getNotSpecifiedText();
      if (result.indexOf('messageKey:') == 0) {
        result = $translate.instant(result.substring('messageKey:'.length));
      }

      return result;
    }

    return function(input, placeholder) {
      if (angular.isUndefined(input) || input === null) {
        return notSpecifiedText(placeholder);
      }

      if (angular.isNumber(input) || angular.isDate(input)) {
        return input; 
      }

      if (angular.isString(input) && (input.trim().length == 0 || input.trim() == 'Not Specified')) {
        return notSpecifiedText(placeholder);
      }

      return input;
    }
  });
