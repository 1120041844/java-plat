package com.work.qrcode.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.common.HybridBinarizer;
import com.work.qrcode.constants.CodeTypeConstant;
import com.work.qrcode.constants.SizeConstant;
import com.work.qrcode.enums.BarCodeFormatEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BarcodeGenerator {
//    public static void main(String[] args) {
//        String data = "224345323";
//        String path = "barcode.png";
//
//        try {
//            BitMatrix matrix = new DataMatrixWriter().encode(data, BarcodeFormat.DATA_MATRIX, 300, 150);
//            Path outputPath = FileSystems.getDefault().getPath(path);
//            MatrixToImageWriter.writeToPath(matrix, "PNG", outputPath);
//            System.out.println("Barcode generated: " + path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) {
//        generator(BarCodeFormatEnum.QR_CODE);
//    }
//
//
//    public static void generator(BarCodeFormatEnum format) {
//        String data = "12345678943"; // 这里是你想要编码的数据
//        String path = "barcode.png"; // 输出文件路径
//
//        try {
//
//            Class clazz = format.getClazz();
//            Writer writer = (Writer) clazz.getDeclaredConstructor().newInstance();
//
//            BitMatrix matrix = writer.encode(data, format.getFormat(), 300, 150); // 可以调整宽度和高度
//            Path outputPath = FileSystems.getDefault().getPath(path);
//            MatrixToImageWriter.writeToPath(matrix, "PNG", outputPath);
//            System.out.println("Barcode generated: " + path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static Result analyzeCode(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(binaryBitmap);
        return result;
    }

    public static void createCode(Integer type, boolean withText, String data, BarCodeFormatEnum format, OutputStream stream) throws IOException {
        Integer width;
        Integer height;
        // 条形码
        if (type == CodeTypeConstant.barCode) {
            width = SizeConstant.BarConstant.width;
            height = SizeConstant.BarConstant.height;
            // 是否带文本
            createBarCode(withText, format, data, width, height, stream);
        } else if (type == CodeTypeConstant.qrCode) {
            // 二维码
            width = SizeConstant.QrCodeConstant.width;
            height = SizeConstant.QrCodeConstant.height;
            createBarCode(false, format, data, width, height, stream);
        }
    }

    private static void createBarCode(boolean withText, BarCodeFormatEnum format, String data, Integer width, Integer height, OutputStream stream) throws IOException {

        BitMatrix bitMatrix = generatorBitMatrix(format, data, width, height);

        if (withText) {
            createImgWithText(bitMatrix, data, width, height, stream);
            return;
        }
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
    }

    private static BitMatrix generatorBitMatrix(BarCodeFormatEnum format, String data, Integer width, Integer height) {
        try {
            Class clazz = format.getClazz();
            Writer writer = (Writer) clazz.getDeclaredConstructor().newInstance();
            return writer.encode(data, format.getFormat(), width, height); // 可以调整宽度和高度
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void createImgWithText(BitMatrix bitMatrix, String data, Integer width, Integer height, OutputStream stream) {

        int textHeight = 30; // 文本高度
        try {
            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 创建一个新的图像，底部留出空间以容纳文本
            BufferedImage finalImage = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = finalImage.createGraphics();

            // 设置背景为白色
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());

            // 绘制条形码
            g.drawImage(barcodeImage, 0, 0, null);

            // 设置字体和颜色
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.BLACK); // 确保文本颜色为黑色

            // 计算文本宽度并居中
            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(data);
            int x = (width - textWidth) / 2; // 计算居中位置

            // 绘制文本
            g.drawString(data, x, height + textHeight - 10); // 绘制文本

            g.dispose();
            // 保存最终图像
            ImageIO.write(finalImage, "PNG", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
