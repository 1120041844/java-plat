package com.work.ai.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImageCompressor {

    /**
     * 压缩图片
     *
     * @param inputFilePath  输入图片文件路径
     * @param outputFilePath 输出图片文件路径
     * @param quality        压缩质量 (0.0 - 1.0)
     * @throws IOException   如果出现IO异常
     */
    public static void compressImage(String inputFilePath, String outputFilePath, float quality) throws IOException {
        // 检查质量范围
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException("Quality must be between 0.0 and 1.0");
        }

        // 读取输入图片
        File inputFile = new File(inputFilePath);
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Cannot read input file: " + inputFilePath);
        }

        // 获取图片写入器
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for JPG format");
        }
        ImageWriter writer = writers.next();

        // 设置输出文件流
        File outputFile = new File(outputFilePath);
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
            writer.setOutput(ios);

            // 设置压缩参数
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }

            // 写入图片
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    public static void main(String[] args) {
        String inputFile = "/Users/jianan/Desktop/aaa/a07d9948f4ed78.png"; // 替换为你的输入图片路径
        String outputFile = "/Users/jianan/Desktop/target/a07d9948f4ed78.png"; // 替换为你的输出图片路径
        float quality = 0.5f; // 压缩质量（越低文件越小，质量越低）

        try {
            compressImage(inputFile, outputFile, quality);
            System.out.println("图片压缩完成，输出路径: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
