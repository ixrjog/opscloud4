package com.sdg.cmdb.util;


import com.google.common.base.Joiner;
import org.springframework.util.StringUtils;

public class SSHKeyUtils {

    /**
     * 计算RsaKey MD5
     *
     * @param pubkey
     * @return
     */
    public static String getMD5(String pubkey) {
        if (StringUtils.isEmpty(pubkey)) return "";
        String[] keys = pubkey.split("\\s+");
        if (keys.length != 3)
            return "";
        String key = Joiner.on(" ").join(keys[0], keys[1]);
        return EncryptionUtil.md5(key);
    }


}
