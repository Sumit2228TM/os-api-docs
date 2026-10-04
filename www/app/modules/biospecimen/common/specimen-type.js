angular.module('os.biospecimen.common')
  .factory('SpecimenTypeUtil', function($q, $http, ApiUrls, PvManager) {

    var typeProps = undefined;

    var typesQ = undefined;

    function transformer(pv) {
      return {specimenClass: pv.parentValue, type: pv.value};
    }

    function loadSpecimenTypes() {
      return PvManager.loadPvsByParent('specimen-class', '', true, transformer, 1000);
    }

    function loadSpecimenTypesWithProps() {
      if (typeProps) {
        var q = $q.defer();
        q.resolve(typeProps);
        return q.promise;
      }

      if (!typesQ) {
        var url = ApiUrls.getBaseUrl() + 'permissible-values';
        var qp = {attribute: 'specimen_type', includeParentValue: true, includeProps: true, maxResults: 1000};
        typesQ = $http.get(url, {params: qp});
      }

      return typesQ.then(
        function(types) {
          typeProps = {};

          angular.forEach(types.data,
            function(type) {
              if (type.parentValue) {
                typeProps[type.parentValue + ':' + type.value] = type;
              } else {
                typeProps[type.value + ':*'] = type;
              }
            }
          );

          return typeProps;
        }
      );
    }

    function loadLocalTypes(options) {
      return function() {
        var q = $q.defer();
        q.resolve(options);
        return q.promise;
      }
    }

    function getSpecimenTypes(formCtrl, options) {
      var loadFn = !options ? loadSpecimenTypes : loadLocalTypes(options);
      return formCtrl.getCachedValues('pvs', 'specimen-type', loadFn);
    }

    function setType(specimen, types, type) {
      for (var i = 0; i < types.length; ++i) {
        if (types[i].type == type) {
          angular.extend(specimen, types[i]);
          break;
        }
      }
    }

    function init(specimen, types) {
      var type = specimen.type;
      if (!type) {
        return;
      }

      setType(specimen, types, type);
    }

    return {
      setClass: function(formCtrl, specimens, options) {
        getSpecimenTypes(formCtrl, options).then(
          function(types) {
            angular.forEach(specimens, function(specimen) { init(specimen, types); });
          }
        );
      },

      getSpecimenTypes: getSpecimenTypes,

      getTypesWithProps: loadSpecimenTypesWithProps
    }
  })
  .directive('osSpecimenTypes', function($timeout, SpecimenTypeUtil, PvManager) {
    var valuesQ = undefined;

    function linker(scope, element, attrs, formCtrl) {
      if (attrs.defaultValue && !scope.specimen.type) {
        scope.specimen.type = attrs.defaultValue;
      }

      scope.types = [];
      scope.model = {value: scope.specimen.type};

      valuesQ = SpecimenTypeUtil.getSpecimenTypes(formCtrl, scope.options);
      valuesQ.then(function(types) { scope.types = types; });

      scope.onTypeSelect = function(type) {
        type = type || {specimenClass: '', type: ''};
        angular.extend(scope.specimen, type);
        scope.onSelect({type: type});
      }

      SpecimenTypeUtil.setClass(formCtrl, [scope.specimen], scope.options);

      scope.$watch('specimen.type',
        function(newVal, oldVal) {
          if (newVal == oldVal) {
            return;
          }

          scope.model = {value: newVal};
          SpecimenTypeUtil.setClass(formCtrl, [scope.specimen], scope.options);
        }
      );
    }

    return {
      restrict: 'E',

      require: '^osFormValidator',

      scope: {
        specimen: '=',
        options:  '=?',
        onSelect: '&'
      },

      replace: true,

      template: 
        '<div>' +
        '  <os-select ng-model="model.value" list="types" group-by="\'specimenClass\'"' +
        '    select-prop="type" display-prop="type" on-select="onTypeSelect($item)" append-to-body="true">' +
        '  </os-select>' +
        '</div>',

      compile: function(tElem, tAttrs) {
        var selectEl = tElem.find('os-select');
        if (tAttrs.hasOwnProperty('mdInput')) {
          selectEl.attr('os-md-input', '');
        }

        if (tAttrs.ngRequired) {
          selectEl.attr('ng-required', tAttrs.ngRequired);
        }

        angular.forEach(tAttrs,
          function(val, attr) {
            if (attr.indexOf('$') != 0) {
              selectEl.attr(attr, val);
            }
          }
        );

        return linker;
      }
    }
  });
