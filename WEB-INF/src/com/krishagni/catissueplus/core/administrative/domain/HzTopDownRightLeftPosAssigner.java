package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.Pair;

public class HzTopDownRightLeftPosAssigner extends RowMajorPositionAssigner {
	@Override
	public Pair<Integer, Integer> getMapIdx(StorageContainer container, Integer row, Integer col) {
		return Pair.make(row - 1, container.getNoOfColumns() - col);
	}

	@Override
	public Pair<Integer, Integer> fromMapIdx(StorageContainer container, Integer rowIdx, Integer colIdx) {
		return Pair.make(rowIdx + 1, container.getNoOfColumns() - colIdx);
	}
}
