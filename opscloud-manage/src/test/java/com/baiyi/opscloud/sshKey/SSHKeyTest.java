package com.baiyi.opscloud.sshKey;

import com.baiyi.opscloud.BaseUnit;
import com.jcraft.jsch.HASH;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author baiyi
 * @Date 2020/2/27 9:44 上午
 * @Version 1.0
 */
public class SSHKeyTest extends BaseUnit {


    /**
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



    public static Map<String, String> getKeyMap(String comment) {
        Map<String, String> keys = new HashMap<>();
        int type = KeyPair.RSA;
        JSch jsch = new JSch();

        try {
            KeyPair kpair = KeyPair.genKeyPair(jsch, type, 2048);
            //私钥
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//向OutPutStream中写入
            kpair.writePrivateKey(baos);
            String privateKeyString = baos.toString();
            //公钥
            baos = new ByteArrayOutputStream();
            kpair.writePublicKey(baos, comment);
            String publicKeyString = baos.toString();
            System.out.println("Finger print: " + kpair.getFingerPrint());
            keys.put("fingerPrint", kpair.getFingerPrint()); // 加入指纹
            kpair.dispose();
            // 得到公钥字符串
            //String publicKeyString = RSAEncrypt.loadPublicKeyByFile(filePath,filename + ".pub");
            System.out.println(publicKeyString.length());
            System.out.println(publicKeyString);
            keys.put("publicKey", publicKeyString); // 公钥

            // 得到私钥字符串
//			String privateKeyString = RSAEncrypt.loadPrivateKeyByFile(filePath,filename);
//			System.out.println(privateKeyString.length());
            System.out.println(privateKeyString);
            keys.put("privateKey", privateKeyString); // 私钥
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }

    private static String[] chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    static String getFingerPrint(HASH hash, byte[] data) {
        try {
            hash.init();
            hash.update(data, 0, data.length);
            byte[] foo = hash.digest();
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < foo.length; ++i) {
                int bar = foo[i] & 255;
                sb.append(chars[bar >>> 4 & 15]);
                sb.append(chars[bar & 15]);
                if (i + 1 < foo.length) {
                    sb.append(":");
                }
            }

            return sb.toString();
        } catch (Exception var6) {
            return "???";
        }
    }

    static String checkTilde(String str) {
        try {
            if (str.startsWith("~")) {
                str = str.replace("~", System.getProperty("user.home"));
            }
        } catch (SecurityException var2) {
        }

        return str;
    }

    static byte[] fromFile(String _file) throws IOException {
        _file = checkTilde(_file);
        File file = new File(_file);
        FileInputStream fis = new FileInputStream(_file);

        try {
            byte[] result = new byte[(int)file.length()];
            int len = 0;

            while(true) {
                int i = fis.read(result, len, result.length - len);
                if (i <= 0) {
                    fis.close();
                    byte[] var9 = result;
                    return var9;
                }

                len += i;
            }
        } finally {
            if (fis != null) {
                fis.close();
            }

        }
    }


    @Test
    void aaa() {
        String a = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDtVLKSMBSDxFBApa+1fmcGG0OHizL6kPfrFY7KMScoILNrhf5y2GoV5WxSSSd73c56YYd5HbfK3CFIjwZ54swDhKEiGkSDA7FOlriv1TTyvhknkDSsnsABibPKtRkP9XT3EzznolwikqWCbANTu1XiIR6EaX5r+rL54mtwE2xqOEKdkbU9wkkd41dEIMcwqcgazzTb3hrUunVFF5JrZXukCkLRRDGtYcXKA4vFOILpqLZiTMW7hPto3F9NGdBIy7ZphD2QUEuVmFgnwpCUb2ps0Ud3uLqdIF+folEHC4rqDBB2Nqgx/vbIB94U3bIJED9zkfOtubC5eUq7IFPrZvPb liangjian@51xianqu.net";
        String f= getFingerprint(a);
        System.err.println(f);


//        try {
//            byte[] kblob = fromFile("/Users/liangjian/.ssh/id_rsa.pub");
//            Class c = Class.forName(JSch.getConfig("md5"));
//            HASH hash = (HASH) ((HASH) c.newInstance());
//            hash.init();
//
//            System.err.println(getFingerPrint(hash, kblob));
//        } catch (Exception e) {
//           e.printStackTrace();
//        }


//        Map<String, String> map = getKeyMap("baiyi@gegejia.com");
//        for (String key : map.keySet())
//            System.err.println(key + ":" + map.get(key));
    }


}
