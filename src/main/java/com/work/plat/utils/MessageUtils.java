package com.work.plat.utils;

import com.work.plat.constants.IResultCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, msgKey, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return msgKey;
        }
    }

    /**
     * 根据状态code获取
     * @param resultCode
     * @return
     */
    public static String get(IResultCode resultCode) {
        try {
            return messageSource.getMessage(resultCode.getCode().toString(), null, resultCode.getMessage(), LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return resultCode.getMessage();
        }
    }

    public static String get(String msgKey,Object... args) {
        try {
            return messageSource.getMessage(msgKey, args, msgKey, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return msgKey;
        }
    }

}