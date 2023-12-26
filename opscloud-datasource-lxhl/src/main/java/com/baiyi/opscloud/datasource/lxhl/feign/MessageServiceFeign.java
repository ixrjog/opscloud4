package com.baiyi.opscloud.datasource.lxhl.feign;

import feign.Param;
import feign.RequestLine;

/**
 * @Author 修远
 * @Date 2022/8/31 7:28 PM
 * @Since 1.0
 */
public interface MessageServiceFeign {

    @RequestLine("POST /SendMT/SendMessage?CorpID={corpId}&Pwd={pwd}&Mobile={mobile}&Content={content}")
    String sendMessage(@Param("corpId") String corpId,
                       @Param("pwd") String pwd,
                       @Param("mobile") String mobile,
                       @Param("content") String content);

}