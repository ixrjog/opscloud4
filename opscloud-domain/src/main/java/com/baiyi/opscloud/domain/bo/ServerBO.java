package com.baiyi.opscloud.domain.bo;

import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:45 下午
 * @Version 1.0
 */
@Builder
@Data
public class ServerBO {
    private OcServer ocServer;
    private OcEnv ocEnv;
}
