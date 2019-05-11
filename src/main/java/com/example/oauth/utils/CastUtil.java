package com.example.oauth.utils;

public class CastUtil {

    /**
     * 将字符串转换为int值
     * @param text 要转换的字符串
     * @return 转换完的int值, 字符串不合法则返回0
     */
    public static int castInt(String text) {
        return castInt(text, 0);
    }

    /**
     * 将字符串转换为int值，如果字符串不合法，返回一个预设值
     * @param text 要转换的字符串
     * @param defaultValue 预设值
     * @return 转换完的int值
     */
    public static int castInt(String text, int defaultValue) {
        int res;
        try {
            res = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            res = defaultValue;
        }
        return res;
    }
}
