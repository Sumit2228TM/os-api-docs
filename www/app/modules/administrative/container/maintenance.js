angular.module('os.administrative.container')
  .controller('ContainerMaintenanceCtrl', function(
    $scope, $modal, container, ScheduledContainerActivity, ContainerActivityLog, Util, AuthorizationService) {

    var lctx;
    function init() {
      $scope.ctx.showTree = false;
      $scope.$on('$destroy', function() { $scope.ctx.showTree = true });

      var permOpts = {resource: 'StorageContainer', operations: ['Update'], sites: [container.siteName]};
      lctx = $scope.mctx = {
        schedActivities: [],
        schedActivitiesState: {
          empty: true,
          loading: true,
          emptyMessage: 'container.maintenance.empty_sched_activities_list',
          loadingMessage: 'container.maintenance.loading_sched_activities_list'
        },

        activityLogs: undefined,
        activityLogsState: {
          empty: true,
          loading: true,
          emptyMessage: 'container.maintenance.empty_activities_log',
          loadingMessage: 'container.maintenance.loading_activities_log'
        },

        allowEdits: AuthorizationService.isAllowed(permOpts),
        exportDetail: { objectType: 'containerActivityLog', params: {containerId: container.id} }
      }

      loadScheduledActivities();
    }

    function loadScheduledActivities() {
      lctx.schedActivitiesState.loading = true;
      ScheduledContainerActivity.query({containerId: container.id, maxResults: 1000}).then(
        function(schedActivities) {
          lctx.schedActivitiesState.loading = false;
          lctx.schedActivitiesState.empty = schedActivities.length <= 0;
          lctx.schedActivities = schedActivities;
          angular.forEach(schedActivities, addUiProps);
        }
      );
    }

    function loadActivityLogs() {
      lctx.activityLogsState.loading = true;
      ContainerActivityLog.query({containerId: container.id}).then(
        function(activityLogs) {
          lctx.activityLogsState.loading = false;
          lctx.activityLogsState.empty = activityLogs.length <= 0;
          lctx.activityLogs = activityLogs;
        }
      ); 
    }

    function addUiProps(schedActivity) {
      schedActivity.$$uiProps = {
        cycleUnit: schedActivity.cycleIntervalUnit.toLowerCase().charAt(0),
        remUnit: schedActivity.reminderIntervalUnit.toLowerCase().charAt(0),
        task: {id: schedActivity.taskId, name: schedActivity.taskName}
      }
    }

    $scope.selectActivitiesLog = function() {
      if (lctx.activityLogs) {
        return;
      }

      loadActivityLogs();
    }

    $scope.addEditSchedActivity = function(toEdit) {
      $modal.open({
        templateUrl: 'modules/administrative/container/addedit-sched-activity.html',
        controller: 'AddEditContainerSchedActivity',
        resolve: {
          activity: function() {
            if (toEdit) {
              return angular.copy(toEdit);
            } else {
              return new ScheduledContainerActivity({containerId: container.id});
            }
          }
        }
      }).result.then(
        function(savedActivity) {
          addUiProps(savedActivity);

          if (toEdit) {
            var idx = lctx.schedActivities.findIndex(function(s) { return s.id == toEdit.id; });
            lctx.schedActivities[idx] = savedActivity;
          } else {
            lctx.schedActivities.push(savedActivity);
            lctx.schedActivitiesState.empty = lctx.schedActivities.length <= 0;
          }
        }
      );
    }

    $scope.archiveSchedActivity = function(activity) {
      var opts = {
        title:      'container.maintenance.confirm_archive_sched_activity_title',
        confirmMsg: 'container.maintenance.confirm_archive_sched_activity_q',
        input:      activity
      };

      Util.showConfirm(opts).then(
        function() {
          var toSave = angular.copy(activity);
          toSave.activityStatus = 'Closed';
          toSave.$saveOrUpdate().then(
            function() {
              var idx = lctx.schedActivities.indexOf(activity);
              lctx.schedActivities.splice(idx, 1);
              lctx.schedActivitiesState.empty = lctx.schedActivities.length <= 0;
            }
          );
        }
      );
    }

    $scope.addEditActivityLog = function(toEdit) {
      $modal.open({
        templateUrl: 'modules/administrative/container/addedit-activity-log.html',
        controller: 'AddEditContainerActivityLogCtrl',
        resolve: {
          activityLog: function() {
            if (toEdit) {
              toEdit = angular.copy(toEdit);
              toEdit.$$uiProps = {
                adHoc: !toEdit.scheduledActivityId,
                task: {id: toEdit.taskId, name: toEdit.taskName},
                activity: {id: toEdit.scheduledActivityId, name: toEdit.scheduledActivityName}
              };
              return toEdit;
            } else {
              return new ContainerActivityLog({
                containerId: container.id,
                '$$uiProps': {adHoc: true},
                activityDate: new Date().getTime()
              });
            }
          }
        }
      }).result.then(
        function(savedActivity) {
          if (toEdit) {
            var idx = lctx.activityLogs.findIndex(function(al) { return al.id == toEdit.id; });
            lctx.activityLogs[idx] = savedActivity;
          } else {
            lctx.activityLogs.unshift(savedActivity);
            lctx.activityLogsState.empty = lctx.activityLogs <= 0;
          }
        }
      );
    }

    $scope.archiveActivityLog = function(activity) {
      var opts = {
        title:      'container.maintenance.confirm_archive_activity_title',
        confirmMsg: 'container.maintenance.confirm_archive_activity_q',
        input:      activity
      };

      Util.showConfirm(opts).then(
        function() {
          var toSave = angular.copy(activity);
          toSave.activityStatus = 'Closed';
          toSave.$saveOrUpdate().then(
            function() {
              var idx = lctx.activityLogs.indexOf(activity);
              lctx.activityLogs.splice(idx, 1);
              lctx.activityLogsState.empty = lctx.activityLogs <= 0;
            }
          );
        }
      );
    }

    init();
  });
