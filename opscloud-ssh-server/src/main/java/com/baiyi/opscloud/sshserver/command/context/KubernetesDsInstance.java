package com.baiyi.opscloud.sshserver.command.context;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
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

    private KubernetesDsInstanceConfig kubernetesDsInstanceConfig;
}
