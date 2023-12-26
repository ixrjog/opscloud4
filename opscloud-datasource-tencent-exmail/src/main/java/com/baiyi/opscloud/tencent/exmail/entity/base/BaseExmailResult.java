package com.baiyi.opscloud.tencent.exmail.entity.base;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/10/14 4:36 下午
 * @Version 1.0
 */
@Data
public class BaseExmailResult {

    private String errcode;
    private String errmsg;

    public boolean isError(){
        return "0".equals(errcode);
    }

}