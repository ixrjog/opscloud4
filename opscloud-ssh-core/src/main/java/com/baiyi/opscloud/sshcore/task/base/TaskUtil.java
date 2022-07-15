package com.baiyi.opscloud.sshcore.task.base;

import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2021/7/22 10:49 上午
 * @Version 1.0
 */
public class TaskUtil {

    public static byte[] toBytes(char[] chars) {
//        Charset cs = StandardCharsets.UTF_8;
//        CharBuffer cb = CharBuffer.allocate(chars.length * 2);
//        cb.put(chars);
//        cb.flip();
//        ByteBuffer bb = cs.encode(cb);
//        return bb.array();
       return String.valueOf(chars).getBytes(StandardCharsets.UTF_8);
    }

}
