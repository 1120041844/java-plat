package com.work.qrcode.enums;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.maxicode.MaxiCodeReader;
import com.google.zxing.oned.*;
import com.google.zxing.oned.rss.RSS14Reader;
import com.google.zxing.oned.rss.expanded.RSSExpandedReader;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.work.qrcode.constants.CodeTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BarCodeFormatEnum {

    /**
     * Aztec 2D barcode format.
     */
    AZTEC(BarcodeFormat.AZTEC, AztecWriter.class),

    /**
     * CODABAR 1D format.
     */
    CODABAR(BarcodeFormat.CODABAR, CodaBarWriter.class),

    /**
     * Code 39 1D format.
     */
    CODE_39(BarcodeFormat.CODE_39, Code39Writer.class),

    /**
     * Code 93 1D format.
     */
    CODE_93(BarcodeFormat.CODE_93, Code93Writer.class),

    /**
     * Code 128 1D format.
     */
    CODE_128(BarcodeFormat.CODE_128, Code128Writer.class),

    /**
     * Data Matrix 2D barcode format.
     */
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX, DataMatrixWriter.class),

    /**
     * EAN-8 1D format.
     */
    EAN_8(BarcodeFormat.EAN_8, EAN8Writer.class),

    /**
     * EAN-13 1D format.
     */
    EAN_13(BarcodeFormat.EAN_13, EAN13Writer.class),

    /**
     * ITF (Interleaved Two of Five) 1D format.
     */
    ITF(BarcodeFormat.ITF, ITFWriter.class),

    /**
     * MaxiCode 2D barcode format.
     */
    MAXICODE(BarcodeFormat.MAXICODE, MaxiCodeReader.class),

    /**
     * PDF417 format.
     */
    PDF_417(BarcodeFormat.PDF_417, PDF417Writer.class),

    /**
     * QR Code 2D barcode format.
     */
    QR_CODE(BarcodeFormat.QR_CODE, QRCodeWriter.class),

    /**
     * RSS 14
     */
    RSS_14(BarcodeFormat.RSS_14, RSS14Reader.class),

    /**
     * RSS EXPANDED
     */
    RSS_EXPANDED(BarcodeFormat.RSS_EXPANDED, RSSExpandedReader.class),

    /**
     * UPC-A 1D format.
     */
    UPC_A(BarcodeFormat.UPC_A, UPCAWriter.class),

    /**
     * UPC-E 1D format.
     */
    UPC_E(BarcodeFormat.UPC_E, UPCEWriter.class),

    /**
     * UPC/EAN extension format. Not a stand-alone format.
     */
    UPC_EAN_EXTENSION(BarcodeFormat.UPC_EAN_EXTENSION, UPCEANWriter.class),
    ;


    private BarcodeFormat format;

    private Class<?> clazz;

    public static BarCodeFormatEnum getFormat(Integer codeType,String format) {
        for (BarCodeFormatEnum value : values()) {
            String name = value.name();
            if (name.equalsIgnoreCase(format)) {
                return value;
            }
        }
        return CodeTypeConstant.barCode == codeType ? CODABAR : QR_CODE;
    }

    public static BarCodeFormatEnum getFormat(BarcodeFormat format) {
        for (BarCodeFormatEnum value : values()) {
            if (value.getFormat() == format) {
                return value;
            }
        }
        return null;
    }

}
