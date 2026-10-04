
angular.module('openspecimen')
  .directive('osUploadAttachments', function($timeout) {
    return {
      restrict: 'E',

      scope: {
        uploadUrl: '=',

        files: '='
      },

      templateUrl: 'modules/common/upload-attachments.html',

      link: function($scope, element, attrs) {
        var inputFiles = $scope.inputFiles = [{}];

        $scope.onUpload = function(filename, result, inputFile) {
          $timeout(
            function() {
              angular.extend(inputFile, {name: filename, fileId: result.fileId});
              if (inputFiles.length == 0 || !!inputFiles[inputFiles.length - 1].fileId) {
                inputFiles.push({});
              }

              if ($scope.files) {
                $scope.files.push(inputFile);
              }
            }
          );
        }

        $scope.removeFile = function(idx) {
          inputFiles.splice(idx, 1);
          if ($scope.files) {
            $scope.files.splice(idx, 1);
          }
        }
      }
    };
  });
