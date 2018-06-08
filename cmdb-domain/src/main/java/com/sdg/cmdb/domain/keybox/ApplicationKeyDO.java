package com.sdg.cmdb.domain.keybox;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/11/2.
 */
public class ApplicationKeyDO implements Serializable {
    private static final long serialVersionUID = 5636567835396978657L;

    private int id;

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    private String passphrase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }


    @Override
    public String toString() {
        return "ApplicationKeyDO{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", passphrase='" + passphrase + '\'' +
                '}';
    }
}
