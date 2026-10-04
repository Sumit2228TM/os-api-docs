
angular.module('os.administrative.models.job', ['os.common.models'])
  .factory('ScheduledJob', function(osModel, $http) {
    var ScheduledJob = osModel('scheduled-jobs');

    ScheduledJob.prototype.getType = function() {
      return 'scheduled_jobs';
    }

    ScheduledJob.prototype.getDisplayName = function() {
      return this.name;
    }

    ScheduledJob.prototype.executeJob = function(args) {
      return $http.post(ScheduledJob.url()  + this.$id() + '/runs', args).then(
        function(result) {
          return result.data;
        }
      );
    }

    ScheduledJob.prototype.getRuns = function(params) {
      return $http.get(ScheduledJob.url() + this.$id() + '/runs', {params: params}).then(
        function(result) {
          return result.data;
        }
      );
    }

    ScheduledJob.prototype.getResultUrl = function(runId) {
      return ScheduledJob.url() + this.$id() + '/runs/' + runId + '/result-file';
    }

    return ScheduledJob;
  });
