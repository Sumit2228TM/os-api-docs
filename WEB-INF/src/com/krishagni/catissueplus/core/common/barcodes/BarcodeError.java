package com.krishagni.catissueplus.core.common.barcodes;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum BarcodeError implements ErrorCode {
	INVALID_FMT,

	ENCODE_ERR;

	@Override
	public String code() {
		return "BARCODE_" + name();
	}
}
