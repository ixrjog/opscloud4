package com.baiyi.opscloud.factory.credential.base;

/**
 * @Author baiyi
 * @Date 2021/10/27 10:20 上午
 * @Version 1.0
 */
public interface ICredentialCustomer {

    /**
     * 凭据是否使用
     * @param credentialId
     * @return
     */
    default boolean hasUsedCredential(int credentialId){
        return countByCredentialId(credentialId) != 0;
    }

    /**
     * 统计使用
     * @param credentialId
     * @return
     */
    int countByCredentialId(int credentialId);

    String getBeanName();

}