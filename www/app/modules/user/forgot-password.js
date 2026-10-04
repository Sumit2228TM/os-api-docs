angular.module('openspecimen')
  .controller('ForgotPasswordCtrl', function($scope, User, $translate)  {
    $scope.userCtx = {
      input: "loginName"
    };
    $scope.response = {};

    var onSendMail = function(result) {
      $scope.response.status = result.status;
      if (result.status == 'ok') {
        $scope.response.message = 'forgot_password.reset_email_sent';
      } else {
        $scope.response.message = 'forgot_password.invalid_login_name';
      }   
    }

    $scope.sendPasswordResetLink = function() {
      var user = {};
      user[$scope.userCtx.input] = $scope.userCtx.inputValue;
      User.sendPasswordResetLink(user).then(onSendMail);
    }
  });
