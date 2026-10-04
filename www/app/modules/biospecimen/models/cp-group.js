
angular.module('os.biospecimen.models.cpgroup', ['os.common.models'])
  .factory('CollectionProtocolGroup', function(osModel, $http)  {
    var CollectionProtocolGroup = osModel('collection-protocol-groups');

    CollectionProtocolGroup.MAX_GROUPS = 2000;

    CollectionProtocolGroup.prototype.getForms = function() {
      return $http.get(CollectionProtocolGroup.url() + this.$id() + '/forms').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    CollectionProtocolGroup.prototype.addForms = function(forms) {
      forms.groupId = this.$id();
      return $http.post(CollectionProtocolGroup.url() + this.$id() + '/forms', forms).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    CollectionProtocolGroup.prototype.removeForms = function(level, formIds) {
      var params = {level: level, formId: formIds};
      return $http.delete(CollectionProtocolGroup.url() + this.$id() + '/forms', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return CollectionProtocolGroup;
  });
