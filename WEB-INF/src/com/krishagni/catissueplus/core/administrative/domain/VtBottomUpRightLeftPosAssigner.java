package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.Pair;

public class VtBottomUpRightLeftPosAssigner extends ColumnMajorPositionAssigner {
	@Override
	public Pair<Integer, Integer> getMapIdx(StorageContainer container, Integer row, Integer col) {
		return Pair.make(container.getNoOfRows() - col, container.getNoOfColumns() - row);
	}

	@Override
	public Pair<Integer, Integer> fromMapIdx(StorageContainer container, Integer rowIdx, Integer colIdx) {
		return Pair.make(container.getNoOfColumns() - colIdx, container.getNoOfRows() - rowIdx);
	}
}
