angular.module('os.administrative.setting.util', [])
  .factory('SettingUtil', function($q, Setting) {
    var settings = {};

    function getKey(module, property) {
      return module + ':' + property;
    }

    function getSetting(module, property) {
      var key = getKey(module, property);

      var setting = settings[key];
      if (!setting || (new Date().getTime() - setting.time) / (60 * 60 * 1000) >= 1) {
        setting = {
          promise: Setting.getByProp(module, property),
          time: new Date().getTime()
        };
        settings[key] = setting;
      }

      setting.promise.then(
        function(value) {
          setting.value = value;
        },

        function() {
          //
          // rejected promise, remove it from cache so that it can
          // be loaded again in subsequent invocations...
          //
          delete settings[key];
        }
      );

      return setting.promise;
    }

    function instant(module, property) {
      getSetting(module, property);
      return settings[getKey(module, property)].value;
    }

    function clearSetting(module, property, newValue) {
      var key = getKey(module, property);
      if (settings[key]) {
        delete settings[key];
      }

      if (newValue) {
        var q = $q.defer();
        q.resolve(newValue);
        settings[key] = {
          promise: q.promise,
          time: new Date().getTime(),
          value: newValue
        };
      }

      updateAppProps(newValue);
    }

    function updateAppProps(newValue) {
      if (!newValue) {
        return;
      }

      if (newValue.module == 'common') {
        if (newValue.name == 'not_specified_text') {
          ui.os.appProps.not_specified = newValue.value;
        } else if (newValue.name == 'search_delay') {
          ui.os.global.filterWaitInterval = newValue.value || 500;
        }
      }
    }

    return {
      getSetting: getSetting,

      clearSetting: clearSetting,

      instant: instant,

      getNotSpecifiedText: function() {
       return (ui.os.appProps.not_specified || '').trim();
      }
    }
  });
