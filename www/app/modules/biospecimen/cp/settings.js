
angular.module('os.biospecimen.cp')
  .factory('CpSettingsReg', function(osDisplayList) {
    var settings = osDisplayList();

    settings.addItem({
      name: 'LabelSettings',
      state: 'cp-detail.settings.labels',
      key: 'cp.label_format.title',
      caption: ''
    });

    settings.addItem({
      name: 'FormSettings',
      state: 'cp-detail.settings.forms',
      key: 'cp.forms.title',
      caption: ''
    });

    settings.addItem({
      name: 'ContainerSettings',
      state: 'cp-detail.settings.container',
      key: 'cp.container.title',
      caption: ''
    });

    settings.addItem({
      name: 'ReportSettings',
      state: 'cp-detail.settings.reporting',
      key: 'cp.reporting.title',
      caption: ''
    });

    settings.addItem({
      name: 'DpSettings',
      state: 'cp-detail.settings.dp',
      key: 'cp.dp.title',
      caption: ''
    });

    return {
      getSettings: settings.getItems,

      addSetting: settings.addItem
    }
  });
