package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.util.bae64.BASE64EncoderUtil;

/**
 * @Author baiyi
 * @Date 2020/3/14 7:28 下午
 * @Version 1.0
 */
public class Base64Utils {

    /**
     * BASE64加密 用于生成SS Qcode
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64EncoderUtil()).encodeBuffer(key);
    }
}
