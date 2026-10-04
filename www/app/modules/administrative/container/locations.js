angular.module('os.administrative.container.locations', ['os.administrative.models'])
  .controller('ContainerLocationsCtrl', function(
    $scope, $state, $modal, container, barcodingEnabled,
    Container, ContainerUtil, Alerts, Specimen, SpecimenUtil, Util) {

    var lctx;

    function init() {
      $scope.ctx.showTree  = true;
      $scope.ctx.viewState = 'container-detail.locations';
      lctx = $scope.lctx = {
        mapState: 'loading',
        input: {labels: '', noFreeLocs: false, vacateOccupants: false, useBarcode: false},
        entityInfo: {},
        barcodingEnabled: barcodingEnabled,
        selected: []
      };

      if (container.noOfRows > 0 && container.noOfColumns > 0) {
        loadMap(container);
      }
    }

    function loadMap(container) {
      $scope.lctx.mapState = 'loading';
      container.getOccupiedPositions().then(
        function(occupancyMap) {
          setMap(occupancyMap);
        },

        function() {
          $scope.lctx.mapState = 'error';
        }
      );
    }

    function setMap(occupancyMap) {
      $scope.lctx.mapState = 'loaded';
      $scope.lctx.pristineMap = $scope.lctx.occupancyMap = occupancyMap;
      $scope.lctx.hasFreeSlots = (occupancyMap.length < container.noOfRows * container.noOfColumns);
      $scope.lctx.hasBlockedSlots = occupancyMap.some(function(slot) { return slot.blocked; });
      $scope.lctx.selected = [];
      $scope.lctx.input.labels = undefined;
    }

    function addSpecimens(labels) {
      var filterOpts = {};
      if (!!$scope.lctx.input.useBarcode) {
        filterOpts.barcode = labels;
        labels = undefined;
      }

      ContainerUtil.getSpecimens(labels, filterOpts).then(
        function(specimens) {
          if (!specimens) {
            return;
          }

          var positions = specimens.map(
            function(specimen) {
              return {occuypingEntity: 'specimen', occupyingEntityId: specimen.id};
            }
          );

          var assignOp = {positions: positions};
          container.assignPositions(assignOp).then(
            function() {
              Alerts.success('container.added_specimens', {spmnsCount: positions.length});
              $scope.lctx.input.labels = undefined;
            }
          );
        }
      );
    }

    function getDupLabels(labels) {
      var labelsMap = {}, result = [];
      for (var i = 0; i < labels.length; ++i) {
        var instance = labelsMap[labels[i]] || 0;
        if (instance == 1) {
          result.push(labels[i]);
        }

        labelsMap[labels[i]] = ++instance;
      }

      return result;
    }

    $scope.showInfo = function(entityType, entityId) {
      var promise;
      if (entityType == 'specimen') {
        promise = Specimen.getById(entityId);
      } else if (entityType == 'container') {
        promise = Container.getById(entityId);
      } else {
        return;
      }

      promise.then(
        function(entity) {
          $scope.ctx.showTree    = false;
          $scope.lctx.entityInfo = {type: entityType, id: entityId};
          $scope.lctx.entityInfo[entityType] = entity;
        }
      );
    }

    $scope.hideInfo = function() {
      $scope.lctx.entityInfo    = {};
      $scope.ctx.showTree       = true;
    }

    $scope.addContainer = function(posOne, posTwo, pos) {
      var params = {
        containerId: '',
        posOne: posOne, posTwo: posTwo, pos: pos,
        siteName: container.siteName,
        parentContainerName: container.name,
        parentContainerId: container.id
      };
      $state.go('container-addedit', params);
    }

    $scope.showUpdatedMap = function() {
      if ($scope.ctx.dimless) {
        return;
      }

      var userOpts = {
        vacateOccupants: $scope.lctx.input.vacateOccupants,
        useBarcode: $scope.lctx.input.useBarcode
      };

      var result = ContainerUtil.assignPositions(
        container,
        $scope.lctx.pristineMap,
        $scope.lctx.input.labels,
        userOpts);

      $scope.lctx.occupancyMap = result.map;

      $scope.lctx.input.noFreeLocs = result.noFreeLocs;
      if (result.noFreeLocs) {
        Alerts.error("container.no_free_locs");
      }
    }

    $scope.assignPositions = function() {
      var labels = Util.splitStr($scope.lctx.input.labels, /,|\t|\n/, false);
      var dups = getDupLabels(labels);
      if (dups.length > 0) {
        Alerts.error('container.dup_labels', {barcodes: $scope.lctx.input.useBarcode, dups: dups.join(', ')});
        return;
      }

      if ($scope.ctx.dimless) {
        addSpecimens(labels);
        return;
      }

      if ($scope.lctx.input.noFreeLocs) {
        Alerts.error("container.no_free_locs");
        return;
      }

      var addedEntities = [], vacatedEntities = [];
      for (var i = 0; i < $scope.lctx.occupancyMap.length; ++i) {
        var pos = $scope.lctx.occupancyMap[i];
        if (!!pos.id) {
          continue;
        }

        if (!pos.occupyingEntityName || pos.occupyingEntityName.trim().length == 0) {
          vacatedEntities.push(pos.oldOccupant);
        } else {
          addedEntities.push(pos);
          delete pos.oldOccupant;
        }
      }

      if (addedEntities.length == 0 && vacatedEntities.length == 0) {
        return;
      }

      var labels = addedEntities.map(
        function(pos) {
          return pos.occupyingEntityName;
        }
      );

      var filterOpts = {exactMatch: true};
      if (!!$scope.lctx.input.useBarcode) {
        filterOpts.barcode = labels;
        labels = undefined;
      }

      ContainerUtil.getSpecimens(labels, filterOpts).then(
        function(specimens) {
          if (!specimens) {
            return;
          }

          var specimensMap = {};
          angular.forEach(specimens, function(spmn) {
            specimensMap[spmn.id] = spmn;
          });

          var positions = [];
          angular.forEach(vacatedEntities, function(entity) {
            if (!specimensMap[entity.occupyingEntityId]) {
              //
              // specimen is not reassigned a new position, vacate it from container
              //
              positions.push({occuypingEntity: 'specimen', occupyingEntityId: entity.occupyingEntityId});
            }
          });

          angular.forEach(addedEntities, function(pos, index) {
            pos.occupyingEntityId = specimens[index].id;
            positions.push(pos);
          });

          var assignOp = {vacateOccupant: $scope.lctx.input.vacateOccupants, positions: positions};
          container.assignPositions(assignOp).then(
            function(latestOccupancyMap) {
              setMap(latestOccupancyMap);
            }
          );
        }
      );
    }

    $scope.toggleCellSelect = function(cell) {
      var selected = $scope.lctx.selected;

      if (cell.selected) {
        if (!cell.id) { // unoccupied cell
          selected.push(cell);
        } else {
          var positions = $scope.lctx.pristineMap;
          for (var i = 0; i < positions.length; ++i) {
            if (positions[i].id == cell.id) {
              selected.push(positions[i]);
              break;
            }
          }
        }
      } else {
        for (var i = $scope.lctx.selected.length - 1; i >= 0; --i) {
          if (cell.posOne == selected[i].posOne && cell.posTwo == selected[i].posTwo) {
            selected.splice(i, 1);
            break;
          }
        }
      }
    }

    $scope.blockPositions = function() {
      var selectedCells = $scope.lctx.selected;
      var positions = [];
      for (var i = 0; i < selectedCells.length; ++i) {
        if (!!selectedCells[i].id) {
          break;
        }

        positions.push({posOne: selectedCells[i].posOne, posTwo: selectedCells[i].posTwo});
      }

      if (positions.length != selectedCells.length) {
        Alerts.error('container.empty_cells_can_be_blocked');
        return;
      }

      container.blockPositions(positions).then(setMap);
    }

    $scope.blockAllPositions = function() {
      container.blockPositions([]).then(setMap);
    }

    $scope.unblockPositions = function() {
      var positions = [];
      var selectedCells = $scope.lctx.selected;
      for (var i = 0; i < selectedCells.length; ++i) {
        if (!selectedCells[i].blocked) {
          break;
        }

        positions.push({posOne: selectedCells[i].posOne, posTwo: selectedCells[i].posTwo});
      }

      if (positions.length != selectedCells.length) {
        Alerts.error('container.blocked_cells_can_be_unblocked');
        return;
      }

      container.unblockPositions(positions).then(setMap);
    }

    $scope.unblockAllPositions = function() {
      container.unblockPositions([]).then(setMap);
    }

    $scope.showColorCoding = function() {
      var typesProps = ContainerUtil.getTypesProps();
      if (!typesProps || Object.keys(typesProps).length == 0) {
        alert('Not yet ready!');
        return;
      }

      $modal.open({
        templateUrl: 'modules/administrative/container/color-coding.html',
        controller: function($scope, $modalInstance) {
          var result = []
          var resultMap = {};
          angular.forEach(lctx.pristineMap,
            function(occupant) {
              if (occupant.occuypingEntity != 'specimen' || !occupant.occupantProps) {
                return;
              }

              var op = occupant.occupantProps;
              var code = ContainerUtil.getColorCode(op.specimenClass, op.type);
              var key = code.specimenClass + ':' + (code.type || '*');
              if (!resultMap[key]) {
                result.push(code);
                resultMap[key] = true;
              }
            }
          );

          $scope.mctx = {colorCodes: result};

          $scope.ok = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });
    }

    init();
  });
