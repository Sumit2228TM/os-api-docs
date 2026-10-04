
angular.module('openspecimen')
  .directive('osPvs', function($q, PvManager) {
    function linker(scope, element, attrs, formCtrl) {
      scope.pvs = [];
      scope.reload = true;
      scope.isSelectedValFetched = false;

      if (attrs.parentVal) {
        scope.$watch(attrs.parentVal,
          function(newVal) {
            if (newVal == undefined) {
              return;
            }

            scope.reload = true;
            loadPvs(formCtrl, scope, null, attrs, newVal);
          }
        );
      }

      scope.searchPvs = function(searchTerm) {
        if (scope.reload) {
          loadPvs(formCtrl, scope, searchTerm, attrs);
        }
      };

      var selectedVal = scope.$eval(attrs.ngModel);
      if (!!selectedVal) {
        scope.searchPvs();
      }
    }

    function loadPvs(formCtrl, scope, searchTerm, attrs, parentVal) {
      var q = undefined;
      if (attrs.options) {
        q = $q.defer();
        q.resolve(getLocalPvs(scope, attrs));
        q = q.promise;
      } else if (attrs.parentVal) {
        var prop = 'P:' + attrs.attribute + ':' + parentVal;
        q = getCachedValues(formCtrl, 'pvs', prop, function() { return _loadPvsByParent(attrs, parentVal); });
      } else {
        var prop = attrs.attribute + ":" + attrs.showOnlyLeafValues + ":S:" + (searchTerm || '');
        q = getCachedValues(formCtrl, 'pvs', prop, function() { return _loadPvs(scope, attrs, searchTerm); });
      }

      q.then(
        function(pvs) {
          setPvs(scope, searchTerm, attrs, pvs);
        }
      );
    }

    function getLocalPvs(scope, attrs) {
      var list = scope.$eval(attrs.options);
      if (!list) {
        list = [];
      }

      return list.map(
        function(v) {
          return addDisplayValue(typeof v == 'object' ? v : {value: v});
        }
      );
    }

    function getCachedValues(formCtrl, group, prop, getter) {
      if (!formCtrl) {
        return getter();
      }

      return formCtrl.getCachedValues(group, prop, getter);
    }

    function _loadPvs(scope, attrs, searchTerm, allStatuses) {
      return PvManager.loadPvs(attrs.attribute, searchTerm, addDisplayValue, attrs.showOnlyLeafValues, allStatuses);
    }

    function _loadPvsByParent(attrs, parentVal) {
      return PvManager.loadPvsByParent(attrs.attribute, parentVal, false, addDisplayValue);
    }

    function setPvs(scope, searchTerm, attrs, pvs) {
      scope.pvs = pvs;
      if (attrs.unique == 'true') {
        scope.pvs = pvs.filter(function(el, pos) { return pvs.indexOf(el) == pos; });
      }

      if (!searchTerm && pvs.length < 100) {
        scope.reload = false;
      } 

      if (!searchTerm && !scope.isSelectedValFetched) {
        // init case
        var selectedVal = scope.$eval(attrs.ngModel);
        checkAndFetchSelectedVal(scope, selectedVal, scope.pvs, attrs);
      }
    }

    //
    // PV dropdown shows only 100 PVs to begin with.
    // The selected PV is not displayed if it is not present in initial list of 100 PVs.
    // This function decides whether to initiate a search to obtain selected PV from backend
    //
    function checkAndFetchSelectedVal(scope, selectedVal, pvs, attrs) {
      scope.isSelectedValFetched = !selectedVal || (selectedVal instanceof Array && selectedVal.length == 0);
      if (scope.isSelectedValFetched) {
        return;
      }

      var selValues = [];
      if (selectedVal instanceof Array) {
        selValues = angular.copy(selectedVal);
      } else {
        selValues = [selectedVal];
      }

      scope.isSelectedValFetched = true;
      for (var i = 0; i < pvs.length && selValues.length > 0; i++) {
        var idx = selValues.indexOf(pvs[i].value);
        if (idx != -1) {
          selValues.splice(idx, 1);
        }
      }

      if (selValues.length == 0) {
        return;
      }

      //
      // Bug OPSMN-5500: Presence of comma in the value makes the API controller believe
      // that the value is made up of 2 PVs. To avoid this, adding an empty string.
      // This prevents the aforementioned behaviour of the API controller
      //
      selValues.push('');

      //
      // selected value is not in list;
      // initiate search to retrieve it from backend
      //
      _loadPvs(scope, attrs, selValues, true).then(
        function(searchPvs) {
          angular.forEach(searchPvs, function(p) { pvs.push(p); });
        }
      );
    }

    function addDisplayValue(input) {
      var displayValue = input.value + ( input.conceptCode ? ' (' + input.conceptCode + ') ' : '' );
      return angular.extend(input, {displayValue: displayValue});
    }
    
    return {
      restrict: 'E',
      require: '?^osFormValidator',
      scope: true,
      replace: true,
      link : linker,
      template: '<os-select refresh="searchPvs($select.search)" list="pvs" on-change="searchPvs"' +
                '  select-prop="value" display-prop="displayValue"> ' +
                '</os-select>'
    };
  });
