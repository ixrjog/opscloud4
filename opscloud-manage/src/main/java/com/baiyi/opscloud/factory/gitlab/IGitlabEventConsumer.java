package com.baiyi.opscloud.factory.gitlab;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitlabNotifyParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:37 上午
 * @Version 1.0
 */
public interface IGitlabEventConsumer {

    List<String> getEventNames();

    /**
     * 消费事件
     *
     * @param instance
     * @param systemHook
     */
    void consumeEventV4(DatasourceInstance instance, GitlabNotifyParam.SystemHook systemHook);

}
