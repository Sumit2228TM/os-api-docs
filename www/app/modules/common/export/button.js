
angular.module('openspecimen')
  .factory('osExportSvc', function(ExportJob, Util, Alerts) {

    function exportRecords(input) {
      var msg = Alerts.info('export.initiated');
      new ExportJob(input).$saveOrUpdate().then(
        function(savedJob) {
          Alerts.remove(msg);

          if (savedJob.status == 'COMPLETED') {
            Alerts.info('export.downloading_file');
            Util.downloadFile(savedJob.fileUrl());
          } else if (savedJob.status == 'FAILED') {
            Alerts.error('export.failed', savedJob);
          } else {
            Alerts.info('export.file_will_be_emailed', savedJob);
          }
        },

        function() {
          Alerts.remove(msg);
        }
      );
    }

    return {
      exportRecords: exportRecords
    }
  })

  .directive('osExport', function(osExportSvc) {
    function linker(scope, element, attrs) {
      scope.export = function() {
        var recordIds = null;
        if (scope.checkList) {
          recordIds = scope.checkList.getSelectedItems().map(function(item) { return item.id; });
        }

        var detail = angular.copy(scope.detail);
        detail.recordIds = recordIds;
        osExportSvc.exportRecords(detail);
      }
    }

    return {
      restrict: 'E',
      scope: {
        detail: '=',
        checkList: '=?'
      },
      replace: true,
      link : linker,
      template: '<button ng-click="export()">' +
                '  <span class="fa fa-download"></span>' +
                '  <span translate="common.buttons.export">Export</span>' +
                '</button>'
    };
  });
