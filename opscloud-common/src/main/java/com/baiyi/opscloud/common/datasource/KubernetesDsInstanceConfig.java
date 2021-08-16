package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsKubernetesConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/6/24 4:46 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KubernetesDsInstanceConfig extends BaseDsInstanceConfig {

    private DsKubernetesConfig.Kubernetes kubernetes;

}
