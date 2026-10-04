package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.Pair;

public class VtTopDownRightLeftPosAssigner extends ColumnMajorPositionAssigner {
	@Override
	public Pair<Integer, Integer> getMapIdx(StorageContainer container, Integer row, Integer col) {
		return Pair.make(col - 1, container.getNoOfColumns() - row);
	}

	@Override
	public Pair<Integer, Integer> fromMapIdx(StorageContainer container, Integer rowIdx, Integer colIdx) {
		return Pair.make(container.getNoOfColumns() - colIdx, rowIdx + 1);
	}
}
