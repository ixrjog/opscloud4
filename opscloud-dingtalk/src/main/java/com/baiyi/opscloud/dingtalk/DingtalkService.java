package com.baiyi.opscloud.dingtalk;

import com.baiyi.opscloud.dingtalk.content.DingtalkContent;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/16 1:35 下午
 * @Since 1.0
 */
public interface DingtalkService {

    Boolean doNotify(DingtalkContent content);

    DingtalkContent getDingtalkContent(String dingtalkToken, String msg);
}
