angular.module('os.administrative.order')
  .controller('OrderItemsCtrl', function($scope, $state, order, CommentsUtil, DistributionLabelPrinter) {
  
    var ctx;

    function init() {
      ctx = $scope.ctx = {
        showRetrieveBtn: false,
        params: {
          listName: 'order-specimens-list-view',
          objectId: order.id,
          hideEmptyColumns: true
        },
        emptyState: {
          loadingMessage: 'specimens.loading_list',
          emptyMessage: 'specimens.empty_list'
        }
      };
      showOrHideRetrieve();
    }

    function showOrHideRetrieve() {
      ctx.showRetrieveBtn = false;
      if (order.status != 'EXECUTED') {
        return;
      }

      order.getOrderItems({maxResults: 1, storedInDistributionContainer: true}).then(
        function(items) {
          ctx.showRetrieveBtn = (items.length > 0);
        }
      );
    }

    $scope.retrieveSpecimens = function() {
      var opts = {
        header: 'specimen_list.retrieve_specimens', headerParams: {},
        placeholder: 'specimen_list.retrieve_reason',
        button: 'specimen_list.retrieve_specimens'
      };

      CommentsUtil.getComments(opts,
        function(comments) {
          order.retrieveSpecimens({comments: comments}).then(
            function(count) {
              ctx.listCtrl.loadList();
              ctx.showRetrieveBtn = false;
            }
          );
        }
      );
    }

    $scope.printLabels = function() {
      var itemIds = ctx.listCtrl.getSelectedItems().map(function(item) { return item.hidden.orderItemId_ });
      if (itemIds.length == 0) {
        alert("No order items selected");
        return;
      }

      DistributionLabelPrinter.printLabels({orderId: order.id, itemIds: itemIds}, order.name + '.csv');
    }

    $scope.setListCtrl = function(listCtrl) {
      ctx.listCtrl = listCtrl;
      ctx.showSearch = listCtrl.haveFilters;

      if (order.status != 'PENDING') {
        return;
      }

      ctx.listCtrl.onDataLoad = function(data) {
        if (!data || !data.columns) {
          return;
        }

        for (var i = 0; i < data.columns.length; ++i) {
          if (!!data.columns[i].expr && data.columns[i].expr.indexOf('Specimen.specimenOrders.status') != -1) {
            data.columns[i].hide = true;
            break;
          }
        }
      }
    }

    $scope.showSpecimen = function(row) {
      $state.go('specimen', {specimenId: row.hidden.specimenId});
    }

    init();
  });
