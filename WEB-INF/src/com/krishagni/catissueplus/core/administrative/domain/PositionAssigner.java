package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.Pair;

public interface PositionAssigner {
	Pair<Integer, Integer> nextPosition(StorageContainer container, Integer row, Integer col);

	Integer toPosition(StorageContainer container, Integer row, Integer col);

	Pair<Integer, Integer> fromPosition(StorageContainer container, Integer position);

	Pair<Integer, Integer> nextAvailablePosition(StorageContainer container, Integer startRow, Integer startCol);

	Pair<Integer, Integer> getMapIdx(StorageContainer container, Integer row, Integer col);

	Pair<Integer, Integer> fromMapIdx(StorageContainer container, Integer rowIdx, Integer colIdx);

	boolean isValidPosition(StorageContainer container, Integer row, Integer col);

	boolean isValidPosition(Integer numRows, Integer numCols, Integer row, Integer col);
}
