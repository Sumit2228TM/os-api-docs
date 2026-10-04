angular.module('os.biospecimen.models.visit', ['os.common.models', 'os.biospecimen.models.form'])
  .factory('Visit', function(osModel, $http, ApiUrls, ApiUtil, CollectionProtocolEvent, Form) {
    var Visit = osModel('visits');
 
    function enrich(visits) {
      angular.forEach(visits, function(visit) {
        visit.totalPrimarySpmns = visit.pendingPrimarySpmns + visit.plannedPrimarySpmnsColl +
          visit.uncollectedPrimarySpmns + visit.unplannedPrimarySpmnsColl;
        visit.reqStorage = visit.storedSpecimens + visit.notStoredSpecimens +
          visit.distributedSpecimens + visit.closedSpecimens;
      });

      return visits;
    };

    function getAnticipatedDate(baseDate, offset, offsetUnit) {
      var result = new Date(baseDate);

      switch (offsetUnit) {
        case 'DAYS':
          result.setDate(result.getDate() + offset);
          break;

        case 'WEEKS':
          result.setDate(result.getDate() + offset * 7);
          break;

        case 'MONTHS':
          result.setMonth(result.getMonth() + offset);
          break;

        case 'YEARS':
          result.setFullYear(result.getFullYear() + offset);
          break;
      }

      return result.getTime();
    }

    Visit.listFor = function(cprId, includeStats, sortByDates) {
      return Visit.query({cprId: cprId, includeStats: !!includeStats, sortByDates: !!sortByDates}).then(enrich);
    };

    Visit.getAnticipatedVisit = function(eventId, regDate) {
      return CollectionProtocolEvent.getById(eventId).then(
        function(event) {
          event.eventId = event.id;
          event.site = event.defaultSite;
          event.cpTitle = event.collectionProtocol;
          event.clinicalDiagnoses = [event.clinicalDiagnosis];
         
          delete event.id;
          delete event.defaultSite;
          delete event.collectionProtocol;
          delete event.clinicalDiagnosis;
          delete event.specimenRequirements;
          delete event.visitNamePrintMode;
          delete event.visitNamePrintCopies;

          if (typeof regDate == 'string') {
            regDate = Date.parse(regDate);
          } else if (regDate instanceof Date) {
            regDate = regDate.getTime();
          }

          if (event.eventPoint != null) {
            var ad = getAnticipatedDate(regDate, event.eventPoint, event.eventPointUnit);
            event.anticipatedVisitDate = getAnticipatedDate(ad, -event.offset, event.offsetUnit);
          }
          delete event.offset;
          delete event.offsetUnit;

          return new Visit(event);
        }
      );
    };

    Visit.getByNameSpr = function(opts) {
      var url = Visit.url() + 'bynamespr/';
      return $http.get(url, {params:opts}).then(function(result) {return result.data});
    };

    function visitFilter(visits, filterfn) {
      var results = [];
      angular.forEach(visits, function(visit) {
        if (filterfn(visit)) {
          results.push(visit);
        }
      });

      return results;
    };

    Visit.completedVisits = function(visits) {
      return visitFilter(visits, function(v) { return v.status == 'Complete'; });
    };

    Visit.anticipatedVisits = function(visits) {
      return visitFilter(visits, function(v) { return !v.status || v.status == 'Pending'; });
    };

    Visit.missedVisits = function(visits) {
      return visitFilter(visits, function(v) { return ['Not Collected', 'Missed Collection'].indexOf(v.status) != -1; });
    };

    Visit.collectVisitAndSpecimens = function(visitAndSpecimens) {
      return $http.post(Visit.url() + 'collect', visitAndSpecimens).then(ApiUtil.processResp);
    };

    Visit.searchVisits = function(detail) {
      return $http.post(Visit.url() + 'match', detail).then(ApiUtil.processResp);
    }

    Visit.getRouteIds = function(visitId) {
      var params = {objectName: 'visit', key: 'id', value: visitId};
      return $http.get(ApiUrls.getBaseUrl() + '/object-state-params', {params: params}).then(
        function(result) {
          return result.data;
        }
      );
    }

    Visit.prototype.getType = function() {
      return 'visit';
    }

    Visit.prototype.getDisplayName = function() {
      return this.name;
    };

    Visit.prototype.getForms = function() {
      return Form.listFor(Visit.url(), this.$id());
    };
    
    Visit.prototype.getRecords = function() {
      var url = Visit.url() + this.$id() + '/extension-records';
      return Form.listRecords(url);
    };

    Visit.prototype.getSprFileUrl = function(pdf) {
      var param = !!pdf ? '?type=pdf' : '';
      return Visit.url() + this.$id() + '/spr-file' + param;
    };

    Visit.prototype.getSprTextUrl = function() {
      return Visit.url() + this.$id() + '/spr-text';
    };

    Visit.prototype.getSprText = function() {
      return $http.get(this.getSprTextUrl()).then(function(result) { return result.data; });
    };

    Visit.prototype.updateSprText = function(sprText) {
      return $http.put(this.getSprTextUrl(), sprText).then(function(result) { return result.data; });
    };

    Visit.prototype.deleteSprFile = function() {
      return $http.delete(this.getSprFileUrl()).then(function(result) { return result.data; });
    };

    Visit.prototype.updateSprLockStatus = function(lock) {
      var url = Visit.url() + this.$id() + '/spr-lock';
      return $http.put(url, {locked: lock}).then(function(result) { return result.data; });
    };

    return Visit;
  });
