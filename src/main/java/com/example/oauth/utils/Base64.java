package com.example.oauth.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

public class Base64 {

    private static Base64 base64;

    /**
     * 获得单列
     * @return
     */
    public static Base64 getInstance() {
        if (base64 == null) {
            synchronized (Base64.class) {
                if (base64 == null) {
                    base64 = new Base64();
                }
            }
        }
        return base64;
    }


    /**
     * BASE64解密
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String parseBASE64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * BASE64加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String createBASE64(String str) {
        byte[] b=null;
        String s=null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(b!=null){
            s=new BASE64Encoder().encode(b);
        }
        return s;

    }
}
