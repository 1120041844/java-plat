package com.work.plat.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileUtils {

    public static String convertToBase64(MultipartFile file) throws IOException {
        byte[] bytes = convertToByteArray(file);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] convertToByteArray(MultipartFile file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
