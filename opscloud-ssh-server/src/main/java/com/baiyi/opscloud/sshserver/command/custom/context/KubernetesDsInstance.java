package com.baiyi.opscloud.sshserver.command.custom.context;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/5 5:24 下午
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KubernetesDsInstance {

    private DatasourceInstance dsInstance;

    private KubernetesConfig kubernetesDsInstanceConfig;

}