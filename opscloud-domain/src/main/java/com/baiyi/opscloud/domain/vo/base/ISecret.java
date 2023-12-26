package com.baiyi.opscloud.domain.vo.base;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:42 下午
 * @Version 1.0
 */
public interface ISecret {

    String getSecret();

    void setPlaintext(String plaintext);

    String getPlaintext();

}