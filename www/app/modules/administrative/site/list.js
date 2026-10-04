angular.module('os.administrative.site.list', ['os.administrative.models'])
  .controller('SiteListCtrl', function($scope, $state, currentUser,
    Site, Util, DeleteUtil, ListPagerOpts, CheckList) {

    var pagerOpts, filterOpts, ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getSitesCount});
      filterOpts = $scope.siteFilterOpts = Util.filterOpts({includeStats: true, maxResults: pagerOpts.recordsPerPage + 1});
      ctx = $scope.ctx = {
        exportDetail: {objectType: 'site'},
        emptyState: {
          empty: true,
          loading: true,
          emptyMessage: 'site.empty_list',
          loadingMessage: 'site.loading_list'
        }
      };

      loadSites(filterOpts);
      Util.filter($scope, 'siteFilterOpts', loadSites);
    }

    function loadSites(filterOpts) {
      ctx.emptyState.loading = true;
      Site.query(filterOpts).then(
        function(siteList) {
          ctx.emptyState.loading = false;
          ctx.emptyState.empty = siteList.length <= 0;
          pagerOpts.refreshOpts(siteList);

          $scope.siteList = siteList;
          $scope.ctx.checkList = new CheckList(siteList);
        }
      );
    };

    function getSiteIds(sites) {
      return sites.map(function(site) { return site.id; });
    }

    function getSitesCount() {
      return Site.getCount($scope.siteFilterOpts);
    }

    $scope.showSiteOverview = function(site) {
      $state.go('site-detail.overview', {siteId: site.id});
    };

    $scope.deleteSites = function() {
      var sites = $scope.ctx.checkList.getSelectedItems();

      var opts = {
        confirmDelete:  'site.delete_sites',
        successMessage: 'site.sites_deleted',
        onBulkDeletion: function() {
          loadSites($scope.siteFilterOpts);
        }
      }

      DeleteUtil.bulkDelete({bulkDelete: Site.bulkDelete}, getSiteIds(sites), opts);
    }

    $scope.pageSizeChanged = function() {
      filterOpts.maxResults = pagerOpts.recordsPerPage + 1;
    }

    init();
  });
