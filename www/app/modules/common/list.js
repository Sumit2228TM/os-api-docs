angular.module('openspecimen')
  .directive('osList', function($http, $timeout, $parse, osRightDrawerSvc, ApiUrls, CheckList, ListPagerOpts, Util) {

    function getUrl() {
      return ApiUrls.getBaseUrl() + 'lists/';
    }

    return {
      restrict: 'E',

      templateUrl: 'modules/common/list.html',

      scope: {
        params: '=',

        itemSref: '@',

        enableSelection: '@',

        fixedDataTmpl: '@',

        actionButtonsTmpl: '@',

        showItem: '&',

        initCtrl: '&',

        emptyState: '=',

        starredExpr: '@',

        starItems: '@',

        toggleItemStar: '&'
      },

      controller: function($scope) {
        var ctrl = this;

        var pagerOpts, listParams, ctx;

        function init() {
          pagerOpts = ctrl.pagerOpts = new ListPagerOpts({listSizeGetter: getListSize});
          listParams = ctrl.listParams = {maxResults: pagerOpts.recordsPerPage + 1};

          var emptyState = $scope.emptyState || {};
          emptyState.loading = emptyState.empty = true;
          ctx = {emptyState: emptyState};

          $http.get(getUrl() + 'config', {params: $scope.params}).then(
            function(resp) {
              if (!resp.data) {
                alert("The list is not configured");
                return;
              }

              ctx = $scope.ctx = {
                hideEmptyColumns: resp.data.hideEmptyColumns,
                filtersCfg: resp.data.filters,
                filters: Util.filterOpts({}),
                data: {},
                listSize: -1,
                pagerOpts: pagerOpts,
                emptyState: emptyState
              };

              ctrl.haveFilters = ctx.filtersCfg && ctx.filtersCfg.length > 0;
              Util.filter($scope, 'ctx.filters', loadList);

              if ($scope.initCtrl) {
                $scope.initCtrl({$list: ctrl});
              }

              $timeout(loadList);
            }
          );
        }

        function sortBy(column) {
          if (ctx.sortBy && ctx.sortBy.expr == column.expr) {
            ctx.sortBy.direction = (ctx.sortBy.direction == 'asc') ? 'desc' : 'asc';
          } else {
            if (ctx.sortBy) {
              ctx.sortBy.direction = undefined;
            }

            ctx.sortBy = column;
            ctx.sortBy.direction = 'asc';
          }

          loadList();
        }

        function loadList() {
          var params = angular.extend({}, $scope.params);
          angular.extend(params, listParams);
          if (pagerOpts.$$pageSizeChanged > 0) {
            params.includeCount = false;
          }

          if (ctx.sortBy) {
            params.orderBy        = ctx.sortBy.expr;
            params.orderDirection = ctx.sortBy.direction;
          }

          ctx.emptyState.loading = ctx.emptyState.empty = true;
          $http.post(getUrl() + 'data', getFilters(), {params: params}).then(
            function(resp) {
              ctx.data = resp.data;
              if (params.includeCount) {
                ctx.listSize = resp.data.size;
              }

              if (ctx.hideEmptyColumns) {
                hideEmptyColumns(ctx.data);
              }

              showStarredItems($scope.starredExpr, ctx.data);

              pagerOpts.refreshOpts(resp.data.rows);
              if (ctx.data.rows.length > 12 && ctrl.haveFilters && ctrl.autoSearchOpen != false) {
                osRightDrawerSvc.open();
              }

              if (ctrl.enableSelection) {
                ctrl.checkList = $scope.checkList = new CheckList(ctx.data.rows);
              }

              if (ctx.sortBy) {
                var column = ctx.data.columns.find(function(c) { return c.expr == ctx.sortBy.expr; });
                if (column) {
                  ctx.sortBy = column;
                  column.direction = params.orderDirection;
                } else {
                  ctx.sortBy = undefined;
                }
              }

              ctx.emptyState.loading = false;
              ctx.emptyState.empty = (!ctx.data || !ctx.data.rows || ctx.data.rows.length <= 0);
              ctrl.onDataLoad(ctx.data);
            }
          );
        }

        function getFilters() {
          var filters = [];
          if (ctrl.filtersCtrl) {
            filters = ctrl.filtersCtrl.getFilters();
          }

          return filters;
        }

        function getListSize() {
          if (!listParams.includeCount) {
            listParams.includeCount = true;

            var params = angular.extend({}, $scope.params);
            angular.extend(params, listParams);

            return $http.post(getUrl() + 'size', getFilters(), {params: params}).then(
              function(resp) {
                ctx.listSize = +resp.data.size;
                return {count: ctx.listSize};
              }
            );
          } else {
            return {count: ctx.listSize};
          }
        }

        function getExpressionValues(expr, searchTerm) {
          var params = angular.extend({expr: expr, searchTerm: searchTerm}, $scope.params);
          return $http.get(getUrl() + 'expression-values', {params: params}).then(
            function(resp) {
              return resp.data;
            }
          );
        }

        function hideEmptyColumns(data) {
          angular.forEach(data.columns,
            function(column, idx) {
              column.hide = data.rows.every(
                function(row) {
                  return row.data[idx] == 'Not Specified' || (!row.data[idx] && row.data[idx] != 0);
                }
              );
            }
          );
        }

        function showStarredItems(starredExpr, data) {
          if (!starredExpr) {
            return;
          }

          var pe = $parse(starredExpr);
          angular.forEach(data.rows,
            function(row) {
              row.$$starred = pe({row: row});
            }
          );
        }

        this.getSelectedItems = function() {
          if (!$scope.checkList) {
            return [];
          }

          return $scope.checkList.getSelectedItems()
        }

        this.loadList = loadList;

        this.sortBy = sortBy;

        this.getExpressionValues = getExpressionValues;

        this.onDataLoad = function(data) {
          // no-op
        }

        init();
      },

      controllerAs: '$list',

      link: function(scope, element, attrs, ctrl) {
        ctrl.enableSelection = (scope.enableSelection == 'true' || scope.enableSelection == true);
        if (ctrl.enableSelection) {
          ctrl.checkList = scope.checkList = new CheckList([]);
        }

        scope.setFiltersCtrl = function(filtersCtrl) {
          ctrl.filtersCtrl = filtersCtrl;
        }

        scope.pageSizeChanged = function(newPageSize) {
          ctrl.listParams.maxResults = ctrl.pagerOpts.recordsPerPage + 1;
          ctrl.loadList();
        }

        scope.loadFilterValues = function(expr) {
          return ctrl.getExpressionValues(expr);
        }

        scope.sortBy = function(column) {
          ctrl.sortBy(column);
        }
      }
    }
  });
