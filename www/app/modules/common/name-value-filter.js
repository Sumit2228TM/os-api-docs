
angular.module('os.common')
  .filter('osNameValueText', function() {
    return function(nvList, name, value) {
      name  = name  || 'name';
      value = value || 'value';

      if (nvList === null || nvList === undefined) {
        return '';
      }

      if (!(nvList instanceof Array)) {
        return nvList;
      }

      return nvList.map(
        function(nv) {
          return nv[value] + (!!nv[name] ? ' (' + nv[name] + ')' : '');
        }
      ).join(', ');
    }
  })

