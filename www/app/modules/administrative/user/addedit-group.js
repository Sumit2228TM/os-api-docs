angular.module('os.administrative.user')
  .controller('AddEditUserGroupCtrl', function(
    $scope, $state, group, currentUser, Alerts, UserGroup, ItemsHolder, DeleteUtil) {
 
    function init() { 
      $scope.group = group;

      var users = ItemsHolder.getItems('users');
      if (users instanceof Array && users.length > 0) {
        var instituteName = users[0].instituteName;
        for (var i = 0; i < users.length; ++i) {
          if (users[i].instituteName != instituteName) {
            Alerts.error('user.multi_institute_users');
            $scope.back();
            return;
          }
        }

        if (!group.id) {
          group.institute = instituteName;
        }

        group.users = users;
        ItemsHolder.setItems('users', undefined);
      }

      $scope.showInstitute = false;
      if (!group.id && !group.institute) {
        if (!currentUser.admin) {
          group.institute = currentUser.instituteName;
        } else {
          $scope.showInstitute = true;
        }
      }
    }

    $scope.saveOrUpdate = function() {
      var promise = group.$saveOrUpdate().then(
        function(savedGroup) {
          if (!savedGroup) {
            return;
          }

          $scope.back();
        }
      );
    }

    $scope.delete = function() {
      DeleteUtil.delete($scope.group, {
        onDeleteState: 'user-groups',
        deleteWithoutCheck: true
      });
    }

    init();
  }
);
