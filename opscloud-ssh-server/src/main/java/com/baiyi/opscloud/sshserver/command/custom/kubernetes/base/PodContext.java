package com.baiyi.opscloud.sshserver.command.custom.kubernetes.base;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/7/8 9:29 上午
 * @Version 1.0
 */
@Data
@Builder
public class PodContext {

    private String kubernetesInstanceName;

    private String instanceUuid;

    private String namespace;

    private String podName;

    private String podIp;

    private Set<String> containerNames;

    @Override
    public String toString(){
        return JSONUtil.writeValueAsString(this);
    }

}