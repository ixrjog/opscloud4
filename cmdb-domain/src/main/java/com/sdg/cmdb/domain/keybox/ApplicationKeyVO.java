package com.sdg.cmdb.domain.keybox;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/5/8.
 */
public class ApplicationKeyVO implements Serializable {
    private static final long serialVersionUID = -927944517187672204L;

    private int id;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 指纹
     */
    private String md5;

    /**
     * 原始的私钥
     */
    private String originalPrivateKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getOriginalPrivateKey() {
        return originalPrivateKey;
    }

    public void setOriginalPrivateKey(String originalPrivateKey) {
        this.originalPrivateKey = originalPrivateKey;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public ApplicationKeyVO(ApplicationKeyDO applicationKeyDO) {
        this.publicKey = applicationKeyDO.getPublicKey();
        this.privateKey = applicationKeyDO.getPrivateKey();
    }

    public ApplicationKeyVO(){
    }

    @Override
    public String toString() {
        return "ApplicationKeyVO{" +
                "id=" + id +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", originalPrivateKey='" + originalPrivateKey + '\'' +
                '}';
    }


}
