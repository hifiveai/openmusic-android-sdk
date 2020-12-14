package com.hfliveplayer.sdk.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncodUtils {
    public static String isoToUTF(String string) throws Exception {
        //判断是否为空
        if (null != string) {
            String result = new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //判断是乱码 (GBK包含全部中文字符；UTF-8则包含全世界所有国家需要用到的字符。)
            if (!(Charset.forName("GBK").newEncoder().canEncode(result))) {
                return new String(string.getBytes(StandardCharsets.ISO_8859_1), Charset.forName("GBK")); //转码GBK
            }else {
                return result;
            }
        } else {
            return "";
        }
    }
}
