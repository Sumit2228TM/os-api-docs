angular.module('os.biospecimen.models.specimenkit', ['os.common.models'])
  .factory('SpecimenKit', function(osModel, $http) {

    var SpecimenKit = osModel('specimen-kits');

    SpecimenKit.list = function(opts) {
      var defOpts = {includeStats: true};
      return SpecimenKit.query(angular.extend(defOpts, opts || {}));
    }

    SpecimenKit.prototype.generateReport = function() {
      return $http.get(SpecimenKit.url() + this.$id() + "/report").then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return SpecimenKit;
});
