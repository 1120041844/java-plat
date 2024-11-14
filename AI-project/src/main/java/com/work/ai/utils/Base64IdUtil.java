package com.work.ai.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

public class Base64IdUtil {

    public static String generateShortId() {
        // 生成随机 UUID
        UUID uuid = UUID.randomUUID();
        // 转换为字节数组并进行 Base64 编码
        byte[] bytes = uuid.toString().getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        // 返回前 8 位
        return base64.substring(0, Math.min(base64.length(), 8));
    }

    private static String generateUuid() {
        // 生成随机 UUID
        UUID uuid = UUID.randomUUID();
        // 转换为字节数组并进行 Base64 编码
        byte[] bytes = uuid.toString().getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return base64.toUpperCase(Locale.ROOT);
    }

}
