package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/2/27 1:04 下午
 * @Version 1.0
 */
public enum CredentialType {

    PASSWORD(1, "password"),
    SSH_PUB_KEY(2, "sshPubKey"),
    SSH_PRIVATE_KEY(3, "sshPrivateKey");

    private int type;
    private String name;

    CredentialType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static String getName(int type) {
        for (CredentialType credentialType : CredentialType.values()) {
            if (credentialType.getType() == type) {
                return credentialType.getName();
            }
        }
        return "Null";
    }
}
