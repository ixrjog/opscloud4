package com.baiyi.opscloud.common.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/28 12:17 下午
 * @Version 1.0
 */
public class SSHKeyUtils {

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
            //System.out.println("Finger print: " + kpair.getFingerPrint());
            keys.put("fingerPrint", kpair.getFingerPrint()); // 加入指纹
            kpair.dispose();
            // 得到公钥字符串
            //String publicKeyString = RSAEncrypt.loadPublicKeyByFile(filePath,filename + ".pub");
            //System.out.println(publicKeyString.length());
            //System.out.println(publicKeyString);
            keys.put("publicKey", publicKeyString); // 公钥
            // 得到私钥字符串
//			String privateKeyString = RSAEncrypt.loadPrivateKeyByFile(filePath,filename);
//			System.out.println(privateKeyString.length());
            //System.out.println(privateKeyString);
            keys.put("privateKey", privateKeyString); // 私钥
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
}
