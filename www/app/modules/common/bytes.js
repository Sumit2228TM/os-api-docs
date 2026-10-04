angular.module('openspecimen')
  .filter('osBytes', function() {
    var units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];

    return function(size, precision) {
      if (isNaN(parseFloat(size)) || !isFinite(size) || size == 0) {
        return '-';
      }

      var order = Math.floor(Math.log(size) / Math.log(1024));
      return (size / Math.pow(1024, order)).toFixed(precision || 1) + ' ' + units[order];
    }
  });
