package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Set;

import com.krishagni.catissueplus.core.common.Pair;

public abstract class RowMajorPositionAssigner implements PositionAssigner {
	@Override
	public Pair<Integer, Integer> nextPosition(StorageContainer container, Integer row, Integer col) {
		++col;
		if (col > container.getNoOfColumns()) {
			++row;
			col = 1;
		}

		return Pair.make(row, col);
	}

	@Override
	public Integer toPosition(StorageContainer container, Integer row, Integer col) {
		return (row - 1) * container.getNoOfColumns() + col;
	}

	@Override
	public Pair<Integer, Integer> fromPosition(StorageContainer container, Integer position) {
		Integer row = (position - 1) / container.getNoOfColumns() + 1;
		Integer column = (position - 1) % container.getNoOfColumns() + 1;
		return Pair.make(row, column);
	}

	@Override
	public Pair<Integer, Integer> nextAvailablePosition(StorageContainer container, Integer startRow, Integer startCol) {
		Set<Integer> occupiedPositionOrdinals = container.occupiedPositionsOrdinals();

		for (int y = startRow; y <= container.getNoOfRows(); ++y) {
			for (int x = startCol; x <= container.getNoOfColumns(); ++x) {
				int pos = toPosition(container, y, x);
				if (!occupiedPositionOrdinals.contains(pos)) {
					return Pair.make(y, x);
				}
			}

			startCol = 1;
		}

		return null;
	}

	@Override
	public boolean isValidPosition(StorageContainer container, Integer row, Integer col) {
		return isValidPosition(container.getNoOfRows(), container.getNoOfColumns(), row, col);
	}

	@Override
	public boolean isValidPosition(Integer numRows, Integer numCols, Integer row, Integer col) {
		return row >= 1 && row <=  numRows && col >= 1 && col <= numCols;
	}
}
