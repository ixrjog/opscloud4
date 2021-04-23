package com.baiyi.opscloud.tencent.exmail.convert;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.tencent.exmail.bo.TencentExmailUserBO;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 2:52 下午
 * @Since 1.0
 */
public class TencentExmailUserConvert {

    public static TencentExmailUserBO toBO(JsonNode data) {
        TencentExmailUserBO user =  JSON.parseObject(data.toString(), TencentExmailUserBO.class);
        return user;
    }
}
