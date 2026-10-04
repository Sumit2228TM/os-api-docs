angular.module('openspecimen')
  .directive('datepickerPopup', function ($filter, $timeout, dateParser) {
    function link(scope, element, attrs, ngModel) {
      function validator(modelValue, viewValue) {
        var value = modelValue || viewValue;

        if (!attrs.ngRequired && !value) {
          return true;
        }

        if (angular.isNumber(value)) {
          value = new Date(value);
        } else if (angular.isString(value) && !isNaN(parseInt(value))) {
          value = new Date(parseInt(value));
        }

        if (!value) {
          return true;
        } else if (angular.isDate(value) && !isNaN(value)) {
          return true;
        } else if (angular.isString(value)) {
          var date = new Date(value);
          return !isNaN(date);
        } else {
          return false;
        }
      }

      // View -> Model
      $timeout(function() {
        ngModel.$parsers.unshift(
          function(viewValue) {
            if (viewValue == '' || viewValue == undefined || viewValue == null) {
              return viewValue;
            }

            var result = dateParser.parse(viewValue, attrs.datepickerPopup);
            return (result == undefined || result == null) ? undefined : viewValue;
          }
        );

        ngModel.$validators.date = validator;
      });



      ngModel.$parsers.push(function(val) {
        try {
          if (!val) {
            return '';
          }

          if (attrs.dateOnly != "true") {
            return new Date(val).getTime();
          } else {
            //
            // TODO: This has been to address some SG timezone
            // issue. Need to check whether sending epoch time
            // without time component helps
            //
            return $filter('date')(val, attrs.dateOnlyFmt || 'yyyy-MM-dd');
          }
        } catch (e) {
          return val;
        }
      });
    }

    return {
      restrict: 'A',
      require: 'ngModel',
      link: link
    };
  })
  
  .directive ('osDatePicker', function ($rootScope, $timeout, $parse) {
    var inputAttrs = ['name', 'required', 'placeholder'];
    
    function linker (scope, element, attrs) {
      scope.datePicker = {isOpen: false};
      scope.showDatePicker = function () {
        $timeout(function () {
          scope.datePicker.isOpen = true; 
        });
      };

      var value = $parse(attrs.date);
      if (!value(scope) && attrs.hasOwnProperty('defaultCurrentDate')) {
        value.assign(scope, new Date());
      }
    }
    
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'modules/common/datepicker.html',
      scope: true,
      compile: function (tElement, tAttrs) {
        if (tAttrs.mdType == 'true') {
          tElement.children().find('div')
            .attr('os-md-input', '')
            .attr('placeholder', tAttrs.placeholder)
            .attr('ng-model', tAttrs.date)
        }

        var inputEl = tElement.find('input');
        angular.forEach(inputAttrs, function(inputAttr) {
          if (angular.isDefined(tAttrs[inputAttr])) {
            inputEl.attr(inputAttr, tAttrs[inputAttr]);
          } 
        });

        if (tAttrs.ngRequired) {
          inputEl.attr("ng-required", tAttrs.ngRequired);
        }

        if (tAttrs.ngDisabled) {
          inputEl.attr('ng-disabled', tAttrs.ngDisabled);
        }

        if (tAttrs.onChange) {
          inputEl.attr('ng-change', tAttrs.onChange);
        }

        inputEl.attr('ng-model', tAttrs.date)
          .attr('date-only', tAttrs.dateOnly)
          .attr('date-only-fmt', tAttrs.dateOnlyFmt)
          .attr('datepicker-append-to-body', tAttrs.appendToBody || false);

        var fmt = tAttrs.dateFormat;
        if (!fmt) {
          fmt = $rootScope.global.shortDateFmt;
        }
        inputEl.attr('datepicker-popup', fmt);

        return linker;
      }
    };
  });
