package com.baiyi.opscloud.factory.gitlab.converter;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.zabbix.constant.SeverityType;

/**
 * @Author baiyi
 * @Date 2021/10/29 1:45 下午
 * @Version 1.0
 */
public class SystemHookConverter {

    public static Event toEvent(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook){
        return Event.builder()
                .instanceUuid(instance.getUuid())
                .eventId(IdUtil.buildUUID())
                .eventIdDesc("GITLAB_EVENT")
                .eventName(systemHook.getEvent_name())
                .eventMessage(JSONUtil.writeValueAsString(systemHook))
                .priority(SeverityType.DEFAULT.getType())
                .isActive(false)
                .build();
    }

}
