package com.baiyi.caesar.datasource.model;

import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
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
