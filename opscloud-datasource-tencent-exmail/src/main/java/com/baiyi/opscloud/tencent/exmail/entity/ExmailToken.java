package com.baiyi.opscloud.tencent.exmail.entity;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.tencent.exmail.entity.base.BaseExmailModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/10/14 4:34 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExmailToken extends BaseExmailModel implements Serializable {

    private static final long serialVersionUID = 7304652109423141557L;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
