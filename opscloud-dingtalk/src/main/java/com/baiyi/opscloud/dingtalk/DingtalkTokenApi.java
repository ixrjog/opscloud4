package com.baiyi.opscloud.dingtalk;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 10:53 上午
 * @Since 1.0
 */
public interface DingtalkTokenApi {

    String getDingtalkToken(String uid) throws RuntimeException;

    String reGetDingtalkToken(String uid);
}
