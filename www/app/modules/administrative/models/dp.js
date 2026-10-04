
angular.module('os.administrative.models.dp', ['os.common.models'])
  .factory('DistributionProtocol', function(osModel, $http, Container) {
    var DistributionProtocol =
      osModel(
        'distribution-protocols',
        function(dp) {
          dp.consentModel = osModel('distribution-protocols/' + dp.$id() + '/consent-tiers');

          dp.consentModel.prototype.getDisplayName = function() {
            return this.consentStmtCode;
          }

          dp.consentModel.prototype.getType = function() {
            return 'consent';
          }
        }
      );

    function updateActivityStatus(dp, status) {
      return $http.put(DistributionProtocol.url() + dp.$id() + '/activity-status', {activityStatus: status}).then(
        function(result) {
          return new DistributionProtocol(result.data);
        }
      );
    }

    DistributionProtocol.MAX_DPS = 2000;

    DistributionProtocol.prototype.getType = function() {
      return 'distribution_protocol';
    }

    DistributionProtocol.prototype.getDisplayName = function() {
      return this.title;
    }

    DistributionProtocol.prototype.getConsentTiers = function() {
      return this.consentModel.query();
    };

    DistributionProtocol.prototype.newConsentTier = function(consentTier) {
      return new this.consentModel(consentTier);
    };

    DistributionProtocol.prototype.close = function() {
      return updateActivityStatus(this, 'Closed');
    }
    
    DistributionProtocol.prototype.reopen = function() {
      return updateActivityStatus(this, 'Active');
    }

    DistributionProtocol.prototype.historyExportUrl = function() {
      var params = '?dpId=' + this.$id() + '&groupBy=specimenType,anatomicSite,pathologyStatus';
      return DistributionProtocol.url() + '/orders-report' + params;
    }

    DistributionProtocol.prototype.getOrderExtnCtxt = function() {
      return DistributionProtocol.getOrderExtnCtxt(this.$id());
    }

    DistributionProtocol.prototype.reserveSpecimens = function(payload) {
      payload = payload || {};
      payload.dpId = this.$id();
      return $http.put(DistributionProtocol.url() + this.$id() + '/reserved-specimens', payload).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    DistributionProtocol.prototype.getReservedSpecimens = function(filterOpts) {
      return $http.get(DistributionProtocol.url() + this.$id() + '/reserved-specimens', {params: filterOpts}).then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    DistributionProtocol.prototype.getReservedSpecimensCount = function(filterOpts) {
      return $http.get(DistributionProtocol.url() + this.$id() + '/reserved-specimens-count', {params: filterOpts}).then(
        function(resp) {
          return +resp.data.count;
        }
      );
    }

    DistributionProtocol.prototype.hasDistributionContainers = function() {
      return $http.get(Container.url(), {params: {dpShortTitle: [this.shortTitle], maxResults: 1}}).then(
        function(resp) {
          return resp.data.length > 0;
        }
      );
    }

    DistributionProtocol.prototype.star = function() {
      return $http.post(DistributionProtocol.url() + this.$id() + '/labels', {}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    DistributionProtocol.prototype.unstar = function() {
      return $http.delete(DistributionProtocol.url() + this.$id() + '/labels').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    DistributionProtocol.getOrders = function(params) {
      return $http.get(DistributionProtocol.url() + 'orders', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    DistributionProtocol.bulkDelete = function(dpIds) {
      return $http.delete(DistributionProtocol.url(), {params: {id: dpIds}})
        .then(DistributionProtocol.modelArrayRespTransform);
    }

    DistributionProtocol.getOrderExtnCtxt = function(dpId) {
      return $http.get(DistributionProtocol.url() + dpId + '/order-extension-form').then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    return DistributionProtocol;
  });

