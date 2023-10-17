package com.baiyi.opscloud.factory.gitlab.context;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/10/29 2:31 下午
 * @Version 1.0
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class GitLabEventContext {
    private DatasourceInstance instance;
    private GitLabNotifyParam.SystemHook systemHook;
    private Event event;
}
