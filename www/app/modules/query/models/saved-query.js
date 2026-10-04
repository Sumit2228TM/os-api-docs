
angular.module('os.query.models.savedquery', ['os.common.models'])
  .factory('SavedQuery', function($http, osModel) {
    var SavedQuery = osModel('saved-queries');

    var ops = {
      eq:          {name: "eq",         desc: "", code: "&#61;",       symbol: '=',           model: 'EQ'},
      ne:          {name: "ne",         desc: "", code: "&#8800;",     symbol: '!=',          model: 'NE',},
      lt:          {name: "lt",         desc: "", code: "&#60;",       symbol: '<',           model: 'LT'},
      le:          {name: "le",         desc: "", code: "&#8804;",     symbol: '<=',          model: 'LE'},
      gt:          {name: "gt",         desc: "", code: "&#62;",       symbol: '>',           model: 'GT'},
      ge:          {name: "ge",         desc: "", code: "&#8805;",     symbol: '>=',          model: 'GE'},
      any:         {name: "any",        desc: "", code: "all",         symbol: 'any',         model: 'ANY'},
      exists:      {name: "exists",     desc: "", code: "&#8707;",     symbol: 'exists',      model: 'EXISTS'},
      not_exists:  {name: "not_exists", desc: "", code: "&#8708;",     symbol: 'not exists',  model: 'NOT_EXISTS'},
      qin:         {name: "qin",        desc: "", code: "&#8712;",     symbol: 'in',          model: 'IN'},
      not_in:      {name: "not_in",     desc: "", code: "&#8713;",     symbol: 'not in',      model: 'NOT_IN'},
      starts_with: {name: "starts_with",desc: "", code: "&#8963;&#61;",symbol: 'starts with', model: 'STARTS_WITH'},
      ends_with:   {name: "ends_with",  desc: "", code: "&#36;&#61;",  symbol: 'ends with',   model: 'ENDS_WITH'},
      contains:    {name: "contains",   desc: "", code: "&#126;",      symbol: 'contains',    model: 'CONTAINS'},
      and:         {name: 'and',        desc: "", code: 'and',         symbol: 'and',         model: 'AND'},
      or:          {name: 'or',         desc: "", code: 'or',          symbol: 'or',          model: 'OR'},
      intersect:   {name: 'intersect',  desc: "", code: '&#8745;',     symbol: 'pand',        model: 'PAND'},
      not:         {name: 'not',        desc: "", code: 'not',         symbol: 'not',         model: 'NOT'},
      nthchild:    {name: 'nthchild',   desc: "", code: '&#xf1e0;',    symbol: 'nthchild',    model: 'NTHCHILD'},
      between:     {name: 'between',    desc: "", code: '&#xf1e0;',    symbol: 'between',     model: 'BETWEEN'}
    };

    SavedQuery.ops = ops;

    SavedQuery.query = function(filterOpts) {
      return $http.get(SavedQuery.url(), {params: filterOpts}).then(
        function(resp) {
          return resp.data.queries.map(
            function(query) {
              return new SavedQuery(query);
            }
          );
        }
      );
    }

    SavedQuery.list = function(filterOpts) {
      var result = {count: 0, queries: []};
      var params = angular.extend({countReq: false}, filterOpts);
      
      $http.get(SavedQuery.url(), {params: params}).then(
        function(resp) {
          result.count = resp.data.count;
          result.queries = resp.data.queries.map(
            function(query) {
              return new SavedQuery(query);
            }
          );
        }
      );

      return result;
    }

    SavedQuery.getImportQueryDefUrl = function() {
      return SavedQuery.url() + 'definition-file';
    }

    SavedQuery.fromQueryCtx = function(qc) {
      return new SavedQuery({
        id: qc.id,
        title: qc.title,
        selectList: qc.selectedFields,
        filters: getCuratedFilters(qc.filters),
        queryExpression: getCuratedExprNodes(qc.exprNodes),
        cpId: !qc.selectedCp.$$cpGroup ? (qc.selectedCp.id || -1) : undefined,
        cpGroupId: qc.selectedCp.$$cpGroup ? qc.selectedCp.id : undefined,
        drivingForm: qc.drivingForm,
        havingClause: qc.havingClause,
        reporting: qc.reporting,
        wideRowMode: qc.wideRowMode,
        outputColumnExprs: qc.outputColumnExprs,
        caseSensitive: qc.caseSensitive
      })
    }

    SavedQuery.prototype.getQueryDefUrl = function() {
      return SavedQuery.url() + this.$id() + '/definition-file';
    }

    function getCuratedFilters(filters) {
      return filters.map(
        function(filter) {
          if (filter.expr) {
            return {
              id: filter.id, expr: filter.expr, 
              desc: filter.desc, parameterized: filter.parameterized
            };
          } else if (filter.hasSq) {
            return {
              id: filter.id, field: filter.form.name + '.' + filter.field.name,
              op: filter.op.model, subQueryId: filter.subQuery.id
            };
          } else {
            var values = filter.value instanceof Array ? filter.value : [filter.value];
            return {
              id: filter.id, field: filter.form.name + "." + filter.field.name,
              op: filter.op.model, values: values,
              parameterized: filter.parameterized, hideOptions: filter.hideOptions,
              desc: (filter.parameterized && filter.desc) || ''
            };
          }
        }
      );
    };

    function getCuratedExprNodes(exprNodes) {
      return exprNodes.map(
        function(node) {
          if (node.type == 'paren') {
            return {nodeType: 'PARENTHESIS', value: node.value == '(' ? 'LEFT' : 'RIGHT'};
          } else if (node.type == 'op') {
            return {nodeType: 'OPERATOR', value: ops[node.value].model};
          } else if (node.type == 'filter') {
            return {nodeType: 'FILTER', value: node.value};
          }
        }
      );
    }

    return SavedQuery;
  });
