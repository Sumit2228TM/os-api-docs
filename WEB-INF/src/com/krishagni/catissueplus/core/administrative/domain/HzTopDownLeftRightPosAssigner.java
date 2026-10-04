package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.common.Pair;

public class HzTopDownLeftRightPosAssigner extends RowMajorPositionAssigner {
	@Override
	public Pair<Integer, Integer> getMapIdx(StorageContainer container, Integer row, Integer col) {
		return Pair.make(row - 1, col - 1);
	}

	@Override
	public Pair<Integer, Integer> fromMapIdx(StorageContainer container, Integer rowIdx, Integer colIdx) {
		return Pair.make(rowIdx + 1, colIdx + 1);
	}
}
