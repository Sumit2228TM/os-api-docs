
angular.module('os.administrative.form.list', ['os.administrative.models'])
  .controller('FormListCtrl', function(
    $scope, $state, $modal, $translate, currentUser, Form, FormEntityReg, ApiUrls,
    CollectionProtocol, Util, DeleteUtil, Alerts, ListPagerOpts, CheckList) {

    var cpListQ = undefined;
    var pagerOpts, filterOpts, ctx;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getFormsCount});
      filterOpts = $scope.formFilterOpts = Util.filterOpts({
        maxResults: pagerOpts.recordsPerPage + 1,
        excludeSysForms: true,
        includeStats: true
      });

      $scope.formsList = [];
      ctx = $scope.ctx = {
        excludeAttrs: ['excludeSysForms'],
        emptyState: {
          empty: true,
          loading: true,
          emptyMessage: 'form.empty_list',
          loadingMessage: 'form.loading_list'
        }
      };

      loadForms($scope.formFilterOpts);
      loadPvs();
      Util.filter($scope, 'formFilterOpts', loadForms, ['maxResults', 'excludeSysForms']);
    }

    function loadForms(filterOpts) {
      ctx.emptyState.loading = true;
      Form.query(filterOpts).then(function(result) {
        ctx.emptyState.loading = false;
        ctx.emptyState.empty = result.length <= 0;
        pagerOpts.refreshOpts(result);
        $scope.formsList = result;
        $scope.ctx.checkList = new CheckList(result);
      })
    }

    function loadPvs() {
      loadEntities();
      loadCps();
    }

    function loadEntities() {
      FormEntityReg.getEntities().then(
        function(entities) {
          ctx.entities = entities.sort(
            function(e1, e2) {
              return e1.caption < e2.caption ? -1 : (e1.caption > e2.caption  ? 1 : 0);
            }
          );
        }
      );
    }

    function loadCps(searchTerm) {
      if (ctx.defaultCps && ctx.defaultCps.length <= 100) {
        ctx.cps = ctx.defaultCps;
        return;
      }

      $translate('form.all_cps').then(
        function(caption) {
          CollectionProtocol.query({query: searchTerm}).then(
            function(cps) {
              cps.unshift({id: -1, shortTitle: caption});
              if (!searchTerm) {
                ctx.defaultCps = cps;
              }

              ctx.cps = cps;
            }
          );
        }
      );
    }

    function reloadForms() {
      loadForms($scope.formFilterOpts);
    }

    function getCpList() {
      if (!cpListQ) {
        cpListQ = CollectionProtocol.list({detailedList: false, maxResults: CollectionProtocol.MAX_CPS});
      }

      return cpListQ;
    }

    function deleteForm(form) {
      form.$remove().then(
        function(resp) {
          Alerts.success('form.form_deleted', form);
          reloadForms();
        }
      );
    }

    function getFormsCount() {
      return Form.getCount($scope.formFilterOpts);
    }

    function getFormIds(forms) {
      return forms.map(function(form) { return form.formId; });
    }

    $scope.searchCps = loadCps;

    $scope.onCpSelect = function() {
      filterOpts.cpId = filterOpts.cpId || [];
      filterOpts.cpId.push(ctx.cpId);
      ctx.cpId = undefined;
    }

    $scope.openForm = function(form) {
      $state.go('form-addedit', {formId: form.formId});
    }

    $scope.showFormContexts = function(form) {
      form.getFormContexts().then(
        function(formCtxts) {
          var formCtxtsModal = $modal.open({
            templateUrl: 'modules/administrative/form/association.html',
            controller: 'FormCtxtsCtrl',
            resolve: {
              args: function() {
                return {
                  formCtxts: formCtxts,
                  form: form
                }
              },

              cpList: function() {
                return getCpList();
              },

              entities: function(FormEntityReg) {
                return FormEntityReg.getEntities();
              },

              currentUser: function() {
                return currentUser;
              }
            },
            size: 'lg'
          });

          formCtxtsModal.result.then(
            function(reload) {
              if (reload) {
                reloadForms();
              }
            }
          );
        }
      );
    };


    $scope.confirmFormDeletion = function(form) {
      FormEntityReg.getEntities().then(
        function(entities) {
          form.entityMap = {};
          angular.forEach(entities,
            function(entity) {
              form.entityMap[entity.name] = entity.caption;
            }
          );
        }
      );
      form.dependentEntities = [];
      form.getDependentEntities().then(
        function(result) {
          Util.unshiftAll(form.dependentEntities, result);
        } 
      );

      DeleteUtil.confirmDelete({
        entity: form,
        templateUrl: 'modules/administrative/form/confirm-delete.html',
        delete: function () { deleteForm(form); }
      });

    }

    $scope.deleteForms = function() {
      var forms = $scope.ctx.checkList.getSelectedItems();

      var opts = {
        confirmDelete:  'form.delete_forms',
        successMessage: 'form.forms_deleted',
        onBulkDeletion: function() {
          loadForms($scope.formFilterOpts);
        }
      }

      DeleteUtil.bulkDelete({bulkDelete: Form.bulkDelete}, getFormIds(forms), opts);
    }

    $scope.pageSizeChanged = function() {
      filterOpts.maxResults = pagerOpts.recordsPerPage + 1;
    }

    $scope.downloadForm = function(form) {
      Util.downloadFile(ApiUrls.getBaseUrl() + 'forms/' + form.formId + '/definition-zip');
    }

    $scope.showRevisions = function(form) {
      $modal.open({
        templateUrl: 'modules/common/audit/revisions.html',
        controller: function($scope, $modalInstance, revisions) {
          $scope.revisions = revisions;
          $scope.actionsTmpl = 'modules/administrative/form/download_revision.html';

          $scope.done = function() {
            $modalInstance.close(true);
          }

          $scope.downloadRevision = function(revision) {
            var formId = revision.id, revId = revision.rev;
            Util.downloadFile(ApiUrls.getBaseUrl() + 'forms/' + formId + '/revisions/' + revId + '/definition-zip');
          }
        },
        resolve: {
          revisions: function() {
            return form.getRevisions().then(
              function(revisions) {
                revisions.forEach(
                  function(rev) {
                    rev.changedOn = rev.revTime;
                    rev.changedBy = rev.revBy;
                  }
                );

                return revisions;
              }
            );
          }
        }
      });
    }

    init();
  });
