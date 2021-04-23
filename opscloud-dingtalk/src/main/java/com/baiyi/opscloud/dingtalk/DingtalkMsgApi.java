package com.baiyi.opscloud.dingtalk;

import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/18 9:49 上午
 * @Since 1.0
 */
public interface DingtalkMsgApi {

    Long sendAsyncMsg(DingtalkParam.SendAsyncMsg param);
}
