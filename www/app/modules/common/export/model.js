
angular.module('os.common.export')
  .factory('ExportJob', function(osModel) {
    var ExportJob = new osModel('export-jobs');

    ExportJob.prototype.fileUrl = function() {
      return ExportJob.url() + this.$id() + '/output';
    }

    return ExportJob;
  });
