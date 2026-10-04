
angular.module('os.administrative.job.list', ['os.administrative.models'])
  .controller('JobListCtrl', function(
    $scope, $modal, $translate, $state, currentUser,
    Util, ScheduledJob, DeleteUtil, Alerts, ListPagerOpts) {

    var pagerOpts, filterOpts;

    function init() {
      $scope.jobs = [];
      $scope.emptyState = {
        empty: true,
        loading: true,
        loadingMessage: 'jobs.loading_list',
        emptyMessage: 'jobs.empty_list'
      };

      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getJobsCount});
      filterOpts = $scope.filterOpts = {query: undefined, maxResults: pagerOpts.recordsPerPage + 1};

      loadTypes();
      loadJobs(filterOpts);
      Util.filter($scope, 'filterOpts', loadJobs);
    }

    function loadJobs(filterOpts) {
      $scope.emptyState.loading = true;
      ScheduledJob.query(filterOpts).then(
        function(jobs) {
          $scope.emptyState.loading = false;
          $scope.emptyState.empty = jobs.length <= 0;

          angular.forEach(jobs,
            function(job) {
              job.$$editAllowed = currentUser.admin || job.createdBy.id == currentUser.id;
            }
          );

          $scope.jobs = jobs;
          pagerOpts.refreshOpts(jobs);
        }
      );
    }

    function runJob(job, args) {
      args = args || {};
      job.executeJob(args).then(
        function() {
          Alerts.success("jobs.queued_for_exec", job);
        }
      );
    }

    function getJobsCount() {
      return ScheduledJob.getCount($scope.filterOpts);
    }

    function loadTypes() {
      $scope.types = [];
      $translate('jobs.types.INTERNAL').then(
        function() {
          $scope.types = ['INTERNAL', 'EXTERNAL', 'QUERY'].map(
            function(type) {
              return {type: type, caption: $translate.instant('jobs.types.' + type)}
            }
          );
        }
      );
    }
    
    $scope.showJobEdit = function(job) {
      $state.go('job-addedit', {jobId: job.id});
    }

    $scope.executeJob = function(job) {
      if (!job.rtArgsProvided) {
        runJob(job);
        return;
      }

      $modal.open({
        templateUrl: 'modules/administrative/job/args.html',
        controller: function($scope, $modalInstance) {
          $scope.job = job;
          $scope.args = {};

          $scope.ok = function() {
            $modalInstance.close($scope.args); 
          }

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      }).result.then(
        function(args) {
          runJob(job, args);
        }
      );
    }


    $scope.deleteJob = function(job) {
      DeleteUtil.confirmDelete({
        templateUrl: 'modules/administrative/job/confirm-delete.html',
        deleteWithoutCheck: true,
        entity: job,
        delete: function() {
          job.$remove().then(
            function() {
              var idx = $scope.jobs.indexOf(job);
              $scope.jobs.splice(idx, 1);
            }
          );
        }
      });
    }

    init();
  });
