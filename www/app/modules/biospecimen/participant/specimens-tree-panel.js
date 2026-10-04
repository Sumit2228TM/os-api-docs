
angular.module('os.biospecimen.participant')
  .factory('ParticipantSpecimensViewState', function(
    $q, $document, $location, $timeout, $anchorScroll,
    CpConfigSvc, Specimen, ExtensionsUtil) {

    var State = function(cp, cpr, pendingSpmnsDispInterval) {
      this.cp = cp;
      this.cpr = cpr;
      this.rootId = undefined;
      this.selectedSpmn = undefined;
      this.specimens = undefined;
      this.pendingSpmnsDispInterval = pendingSpmnsDispInterval;
      this.onlyOldPendingSpmns = false;
      this.config = {};
    }

    function openSpecimenTree(specimens, openedNodesMap, treeCfg) {
      var defExpandDepth = treeCfg.defaultExpandDepth;
      if (defExpandDepth == undefined || defExpandDepth == null || isNaN(defExpandDepth)) {
        defExpandDepth = 0;
      } else {
        defExpandDepth = +defExpandDepth - 1;
      }

      angular.forEach(specimens,
        function(specimen) {
          if (openedNodesMap) {
            var key = (specimen.id || 'u') + '-' + (specimen.reqId || 'u');
            specimen.isOpened = openedNodesMap[key];
          } else if (defExpandDepth < -1 || specimen.depth <= defExpandDepth) {
            specimen.isOpened = true;
          }
        }
      );
    }

    function openUptoSelected(specimens, selected) {
      if (!specimens || !selected) {
        return;
      }

      var selectedNode = undefined;
      for (var i = 0; i < specimens.length; ++i) {
        var node = specimens[i];
        if ((!!node.id && node.id == selected.id) || (!node.id && !selected.id && node.reqId == selected.reqId)) {
          selectedNode = node;
          break;
        }
      }

      while (selectedNode && !selectedNode.isOpened) {
        selectedNode.isOpened = true;
        selectedNode = selectedNode.parent;
      }
    }

    function isOldVisit(visitDt, interval) {
      if (!visitDt && visitDt != 0) {
        return false; // assuming it is yet to completed visit
      }

      var dt = new Date(visitDt);
      dt.setDate(dt.getDate() + interval);
      return dt.getTime() < Date.now();
    }

    function hidePendingSpmns(specimens, interval) {
      var visitsMap = {}, onlyOldPendingSpmns = true;
      angular.forEach(specimens,
        function(specimen) {
          if (!specimen.visitId || (!!specimen.status && specimen.status != 'Pending')) {
            onlyOldPendingSpmns = false;
            return;
          }

          if (!visitsMap.hasOwnProperty(specimen.visitId)) {
            visitsMap[specimen.visitId] = isOldVisit(specimen.visitDate, interval);
          }

          specimen.$$hideN = visitsMap[specimen.visitId];
          onlyOldPendingSpmns = (onlyOldPendingSpmns && specimen.$$hideN);
        }
      );

      return onlyOldPendingSpmns;
    }

    function prepareSpecimens(state, cpId, specimens) {
      return CpConfigSvc.getSpecimenTreeCfg(cpId).then(
        function(treeCfg) {
          var opts = {hideDerivatives: treeCfg.hideDerivatives};
          state.specimens = Specimen.flatten(specimens, undefined, undefined, undefined, opts);
          openSpecimenTree(state.specimens, state.openedNodesMap, treeCfg);
          angular.forEach(state.specimens,
            function(spmn) {
              ExtensionsUtil.createExtensionFieldMap(spmn, true);
            }
          );

          if (state.pendingSpmnsDispInterval > 0) {
            state.onlyOldPendingSpmns = hidePendingSpmns(state.specimens, state.pendingSpmnsDispInterval);
          }

          openUptoSelected(state.specimens, state.selectedSpmn);
          return state.specimens;
        }
      );
    }

    function filterSpecimens(visitsTab, cpr, specimens) {
      var allowedEvents = cpr.getAllowedEvents(visitsTab);
      return specimens.filter(
        function(s) {
          if (!!s.visitStatus && s.visitStatus != 'Pending') {
            return true;
          }

          return !allowedEvents || !s.eventCode || allowedEvents.indexOf(s.eventCode) != -1;
        }
      );
    }

    State.prototype.getSpecimens = function() {
      var visitsTab = this.config && this.config.visitsTab;
      var cpr = this.cpr;

      if (this.specimens) {
        var q = $q.defer();
        q.resolve(filterSpecimens(visitsTab, cpr, this.specimens));
        return q.promise;
      }

      var that = this;
      if (!this.cp.specimenCentric) {
        return Specimen.listFor(this.cpr.id).then(
          function(specimens) {
            return prepareSpecimens(that, that.cp.id, specimens);
          }
        ).then(
          function(specimens) {
            return filterSpecimens(visitsTab, cpr, specimens);
          }
        );
      } else if (this.selectedSpmn) {
        var answer;
        if (!this.rootId) {
          answer = (this.selectedSpmn.lineage == 'New') ? this.selectedSpmn.id : this.selectedSpmn.getPrimarySpecimenId();
        } else {
          answer = this.rootId;
        }

        return $q.when(answer).then(
          function(rootId) {
            that.rootId = rootId;
            return Specimen.getById(that.rootId).then(
              function(specimen) {
                return prepareSpecimens(that, that.cp.id, [specimen]);
              }
            );
          }
        );
      }
    }

    State.prototype.specimensUpdated = function() {
      var nodesMap = this.openedNodesMap = {};
      angular.forEach(this.specimens,
        function(specimen) {
          var key = (specimen.id || 'u') + '-' + (specimen.reqId || 'u');
          nodesMap[key] = specimen.isOpened;
        }
      );

      this.specimens = undefined;
    }

    State.prototype.getDescTmpl = function() {
      var that = this;
      return CpConfigSvc.getSpecimenTreeCfg(this.cpr.cpId).then(
        function(data) {
          var tmpl = data.summaryDescTmpl || '';
          if (!!tmpl) {
            return tmpl;
          }

          return CpConfigSvc.getCommonCfg(that.cpr.cpId, 'specimenHeader').then(
            function(spmnHeader) {
              return (spmnHeader && spmnHeader.leftTitle) || '';
            }
          );
        }
      );
    }

    State.prototype.selectSpecimen = function(specimen) {
      this.selectedSpmn = specimen;
      openUptoSelected(this.specimens, specimen);
    }

    State.prototype.unselectSpecimen = function() {
      this.selectedSpmn = undefined;
      this.rootId = undefined;
    }

    State.prototype.recordScrollPosition = function(element) {
      this.scrollTop = element.scrollTop();
    }

    State.prototype.scrollTo = function(selector, specimen) {
      var that = this;
      $timeout(function() {
        if (specimen && !that.useScrollTop) {
          $location.hash('specimen-' + (specimen.id || 'na') + '-' + (specimen.reqId || 'na'));
          $anchorScroll();
        } else if (selector && that.scrollTop != undefined) {
          $document.find(selector).scrollTop(that.scrollTop);
        }

        that.useScrollTop = false;
      }, 0, false);
    }

    State.specimensUpdated = function(scope, data) {
      scope.$emit('participantSpecimensUpdated', data);
    }

    return State;
  })
  .directive('osSpecimensTreePanel', function($document, $q) {
    return {
      restrict: 'E',

      scope: {
        viewState: '='
      },

      templateUrl: 'modules/biospecimen/participant/specimens-tree-panel.html',

      link: function(scope, element, attrs) {
        scope.ctx = {
          descTmpl: '',
          specimens: [],
          viewState: scope.viewState
        };

        var containerSelector = '';
        if (attrs.scrollContainer) {
          containerSelector = '.' + attrs.scrollContainer;
        }

        var spmnsQ = scope.viewState.getSpecimens();
        var descQ  = scope.viewState.getDescTmpl();
        $q.all([spmnsQ, descQ]).then(
          function(resps) {
            scope.ctx.specimens = resps[0];
            scope.ctx.descTmpl  = resps[1];
            scope.viewState.scrollTo(containerSelector, scope.viewState.selectedSpmn);
          }
        );

        scope.openSpecimenNode = function(specimen) {
          specimen.isOpened = true;
        }

        scope.closeSpecimenNode = function(specimen) {
          specimen.isOpened = false;
        }

        scope.selectSpecimen = function(specimen) {
          scope.viewState.useScrollTop = true;
        }

        if (containerSelector) {
          $document.find(containerSelector)
            .on('scroll', function(event) {
              scope.viewState.recordScrollPosition(angular.element(event.target));
            }
          );
        }
      }
    }
  });
