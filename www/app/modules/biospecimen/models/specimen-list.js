angular.module('os.biospecimen.models.specimenlist', ['os.common.models'])
  .factory('SpecimenList', function(osModel, $http, $translate, Specimen) {
    var SpecimenList = osModel('specimen-lists',
      function(list) {
        if (list.specimens && list.specimens.length > 0) {
          list.specimens = list.specimens.map(function(spmn) { return new Specimen(spmn)});
        }
      }
    );

    var def_list_name_pattern = /\$\$\$\$user_\d+/;

    function getSpecimensUrl(id) {
      return SpecimenList.url() + id + '/specimens';
    }

    function doSpecimenListOp(listId, specimens, op) {
      var params = "?operation=" + op;
      var ids = specimens.map(function(specimen) { return specimen.id; });
      return $http.put(getSpecimensUrl(listId) + params, ids).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    SpecimenList.prototype.getType = function() {
      return 'specimen_list';
    }

    SpecimenList.prototype.getDisplayName = function() {
      var type = this.getListType(this.owner);
      if (type == 'default_list') {
        return $translate.instant('specimen_list.user_default_list', this.owner);
      } else {
        return this.name;
      }
    }

    SpecimenList.prototype.getSpecimens = function(params) {
      var params = params || {maxResults: 100, includeListCount: true};
      return $http.get(getSpecimensUrl(this.$id()), {params: params}).then(Specimen.modelArrayRespTransform);
    };

    SpecimenList.prototype.getSpecimensCount = function(params) {
      return $http.get(SpecimenList.url() + this.$id() + '/specimens-count', {params: params}).then(
        function(resp) {
          return +resp.data.count;
        }
      );
    };

    SpecimenList.prototype.addSpecimens = function(specimens) {
      return doSpecimenListOp(this.$id(), specimens, 'ADD');
    }

    SpecimenList.prototype.removeSpecimens = function(specimens) {
      return doSpecimenListOp(this.$id(), specimens, 'REMOVE');
    }

    SpecimenList.prototype.updateSpecimens = function(specimens) {
      return doSpecimenListOp(this.$id(), specimens, 'UPDATE');
    }

    SpecimenList.prototype.getListType = function(user) {
      if (this.name === undefined || this.name === null || def_list_name_pattern.exec(this.name) == null) {
        return 'normal_list';
      }

      if (this.owner.id == user.id) {
        return 'default_list';
      } else {
        return 'user_default_list';
      }
    }

    SpecimenList.prototype.addChildSpecimens = function() {
      return $http.post(SpecimenList.url() + this.$id() + '/add-child-specimens').then(
        function(result) {
          return result.data;
        }
      );
    }

    SpecimenList.prototype.generateReport = function(params) {
      return $http.get(SpecimenList.url() + this.$id() + '/report', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    SpecimenList.prototype.star = function() {
      return $http.post(SpecimenList.url() + this.$id() + '/labels', {}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    SpecimenList.prototype.unstar = function() {
      return $http.delete(SpecimenList.url() + this.$id() + '/labels').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return SpecimenList;
  });
