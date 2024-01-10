package com.baiyi.opscloud.tencent.exmail.feign;

import com.baiyi.opscloud.tencent.exmail.entity.ExmailToken;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/10/14 4:14 下午
 * @Version 1.0
 */
public interface TencentExmailTokenFeign {

    @RequestLine("GET /cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}")
    ExmailToken getToken(@Param("corpid") String corpid, @Param("corpsecret") String corpsecret);

}