package com.baiyi.opscloud.common.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2022/3/11 5:33 PM
 * @Version 1.0
 */
public class ByteUtil {

    private ByteUtil() {
    }

    public static byte[] toBytes(char[] chars) {
        Charset cs = StandardCharsets.UTF_8;
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

}
