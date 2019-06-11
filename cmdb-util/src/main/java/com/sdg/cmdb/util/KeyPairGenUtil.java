package com.sdg.cmdb.util;

import com.sdg.cmdb.util.base64.BASE64EncoderUtil;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class KeyPairGenUtil {

    public static final String PUBLIC_KEY_PATH = "/data/www/data/key/id_rsa.pub";
    public static final String PRIVATE_KEY_PATH = "/data/www/data/key/id_rsa";

    public static final String SECRET_PREFIX = "SECRET!";
    /**
     * 指定加密算法为RSA
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 密钥长度，用来初始化
     */
    private static final int KEYSIZE = 1024;
    /**
     * 指定公钥存放文件
     */
    private static String PUBLIC_KEY_FILE = "PublicKey";
    /**
     * 指定私钥存放文件
     */
    private static String PRIVATE_KEY_FILE = "PrivateKey";

    public static void main(String[] args) throws Exception {
        generateKeyPair();
        genKeyPair();
    }

    /**
     * 生成密钥对
     *
     * @throws Exception
     */
    private static void generateKeyPair() throws Exception {

        //     /** RSA算法要求有一个可信任的随机数源 */
        //     SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        //     keyPairGenerator.initialize(KEYSIZE, secureRandom);
        keyPairGenerator.initialize(KEYSIZE);

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();

        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();

        ObjectOutputStream oos1 = null;
        ObjectOutputStream oos2 = null;
        try {
            /** 用对象流将生成的密钥写入文件 */
            oos1 = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
            oos2 = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
            oos1.writeObject(publicKey);
            oos2.writeObject(privateKey);
        } catch (Exception e) {
            throw e;
        } finally {
            /** 清空缓存，关闭文件输出流 */
            oos1.close();
            oos2.close();
        }
    }

    private static void genKeyPair() throws NoSuchAlgorithmException {

        /**RSA算法要求有一个可信任的随机数源 */
        SecureRandom secureRandom = new SecureRandom();

        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        keyPairGenerator.initialize(KEYSIZE, secureRandom);
        //keyPairGenerator.initialize(KEYSIZE);

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();

        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();

        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();

        String publicKeyBase64 = new BASE64EncoderUtil().encode(publicKeyBytes);
        String privateKeyBase64 = new BASE64EncoderUtil().encode(privateKeyBytes);

        System.out.println("publicKeyBase64.length():" + publicKeyBase64.length());
        System.out.println("publicKeyBase64:" + publicKeyBase64);

        System.out.println("privateKeyBase64.length():" + privateKeyBase64.length());
        System.out.println("privateKeyBase64:" + privateKeyBase64);
    }


    public static PublicKey getPublicKey() throws Exception {
        File f = new File(PUBLIC_KEY_PATH);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * 获取私钥
     *
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey() throws Exception {
        File f = new File(PRIVATE_KEY_PATH);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    public static void geration() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(KEYSIZE, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_PATH);
            fos.write(publicKeyBytes);
            fos.close();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            fos = new FileOutputStream(PRIVATE_KEY_PATH);
            fos.write(privateKeyBytes);
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 解密
     *
     * @param secret 格式 SECRET!{BASE64密文}
     * @return
     */
    public static String decrypt(String secret) {
        // TODO 校验 SECRET!头部
        if (secret.indexOf(SECRET_PREFIX, 0) != 0)
            // TODO 没有加密头直接返回
            return secret;

        // TODO 去掉头部的密文
        String sourceSecret = secret.replace(SECRET_PREFIX, "");
        return  EncryptionUtil.decrypt(sourceSecret);
    }


    /**
     * 加密
     *
     * @param source
     * @return
     */
    public static String encrypt(String source) {
        String pwd = EncryptionUtil.encrypt(source);
        return SECRET_PREFIX + pwd;
    }
}


