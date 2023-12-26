package com.baiyi.opscloud.core.model;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/6/23 11:22 上午
 * @Version 1.0
 */
@Data
@Builder
public class DsInstanceContext {

    private DatasourceInstance dsInstance;
    private DatasourceConfig dsConfig;

}