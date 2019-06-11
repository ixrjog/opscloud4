package com.sdg.cmdb.domain.ssh;

import lombok.Data;

import java.io.Serializable;

@Data
public class SshKey implements Serializable {

    private static final long serialVersionUID = 4626999664462237547L;
    /**
     * 标题 通常为mail
     */
    private String title;
    /**
     * 指纹
     */
    private String fingerprint;
    /**
     * ssh-rsa
     */
    private String key;

    public SshKey(String key) {
        this.key = key;
    }

    public SshKey(String key, String title) {
        this.title = title;
        this.key = key;
    }

    public SshKey(String title, String key, String fingerprint) {
        this.title = title;
        this.fingerprint = fingerprint;
        this.key = key;
    }

    public SshKey() {
    }


}
