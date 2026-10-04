package com.krishagni.catissueplus.core.common.barcodes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class BarcodeGeneratorImpl implements BarcodeGenerator {

	@Override
	public String base64Png(String format, String content, int width, int height) {
		try {
			BufferedImage image = encode(format, content, width, height);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", bos);
			return Base64.getEncoder().encodeToString(bos.toByteArray());
		} catch (Exception e) {
			if (e instanceof OpenSpecimenException) {
				throw (OpenSpecimenException) e;
			}

			throw OpenSpecimenException.userError(BarcodeError.ENCODE_ERR, e.getMessage());
		}
	}

	@Override
	public BufferedImage encode(String format, String content, int width, int height) {
		BarcodeFormat barcodeFmt = null;
		try {
			 barcodeFmt = BarcodeFormat.valueOf(format);
		} catch (Exception e) {
			throw OpenSpecimenException.userError(BarcodeError.INVALID_FMT, format);
		}

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, barcodeFmt, width, height);
			return toBufferedImage(bitMatrix);
		} catch (Exception e) {
			throw OpenSpecimenException.userError(BarcodeError.ENCODE_ERR, e.getMessage());
		}
	}

	private BufferedImage toBufferedImage(BitMatrix bitMatrix) {
		int width  = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		int onColor  = BLACK;
		int offColor = WHITE;

		int[] rowPixels = new int[width];
		BitArray row    = new BitArray(width);
		for (int y = 0; y < height; y++) {
			row = bitMatrix.getRow(y, row);
			for (int x = 0; x < width; x++) {
				rowPixels[x] = row.get(x) ? onColor : offColor;
			}

			image.setRGB(0, y, width, 1, rowPixels, 0, width);
		}

		return image;
	}

	private static final int BLACK = 0xFF000000;

	private static final int WHITE = 0xFFFFFFFF;
}
