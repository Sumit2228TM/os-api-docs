
angular.module('os.common')
  .directive('osLetterAvatar', function() {
    var colors = [
      '#ffb900', '#d83b01', '#b50e0e',
      '#e81123', '#b4009e', '#5c2d91',
      '#0078d7', '#00b4ff', '#008272', '#107c10'
    ];

    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        var name = scope.$eval(attrs.name) || '';
        var parts = name.trim().toUpperCase().split(/\s+/);
       
        var avatarLetters = ''; 
        for (var i = 0; i < parts.length; ++i) {
          if (parts[i].trim().length > 0) {
            avatarLetters += parts[i].trim().charAt(0);
          }
        }

        var color;
        if (avatarLetters.length == 0) {
          avatarLetters = '?';
          color = 8 % colors.length;
        } else {
          color = (avatarLetters.charCodeAt(0) - 64) % colors.length;
        }

        element.css({
          'background': colors[color],
          'border-radius': '50%',
          width: '35px',
          height: '35px',
          display: 'inline-block',
          'text-align': 'center',
          padding: '7px 7px'
        }).append(avatarLetters.substring(0, 2));
      }
    }
  });
