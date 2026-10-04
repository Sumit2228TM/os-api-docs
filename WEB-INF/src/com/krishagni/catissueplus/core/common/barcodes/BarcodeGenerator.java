package com.krishagni.catissueplus.core.common.barcodes;

import java.awt.image.BufferedImage;

public interface BarcodeGenerator {
	String base64Png(String format, String content, int width, int height);

	BufferedImage encode(String format, String content, int width, int height);
}
