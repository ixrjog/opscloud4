package com.baiyi.opscloud.common.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/3/5 8:19 下午
 * @Version 1.0
 */
public class SSHUtils {

    /**
     * https://github.com/bastillion-io/Bastillion/blob/master/src/main/java/io/bastillion/manage/util/SSHUtil.java
     * returns public key fingerprint
     *
     * @param publicKey public key
     * @return fingerprint of public key
     */
    public static String getFingerprint(String publicKey) {
        String fingerprint = null;
        if (StringUtils.isNotEmpty(publicKey)) {
            if (publicKey.contains("ssh-")) {
                publicKey = publicKey.substring(publicKey.indexOf("ssh-"));
            } else if (publicKey.contains("ecdsa-")) {
                publicKey = publicKey.substring(publicKey.indexOf("ecdsa-"));
            }
            try {
                KeyPair keyPair = KeyPair.load(new JSch(), null, publicKey.getBytes());
                if (keyPair != null) {
                    fingerprint = keyPair.getFingerPrint();
                }
            } catch (JSchException ex) {
            }

        }
        return fingerprint;

    }

}
