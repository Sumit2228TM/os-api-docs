angular.module('os.biospecimen.specimenkit', ['ui.router', 'os.biospecimen.models'])
  .config(function($stateProvider) {
    $stateProvider
      .state('specimen-kit-list', {
        url:'/specimen-kits',
        templateUrl:'modules/biospecimen/participant/specimen-kit/list.html',
        controller: 'SpecimenKitListCtrl',
        parent: 'cp-view'
      })
      .state('specimen-kit-detail', {
        url: '/specimen-kits/:kitId',
        templateUrl: 'modules/biospecimen/participant/specimen-kit/detail.html',
        resolve: {
          specimenKit: function($stateParams, SpecimenKit) {
            return SpecimenKit.getById($stateParams.kitId);
          }
        },
        controller: 'SpecimenKitDetailCtrl',
        parent: 'cp-view'
      })
      .state('specimen-kit-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/participant/specimen-kit/overview.html',
        parent: 'specimen-kit-detail'
      });
  });
