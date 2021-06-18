package com.baiyi.caesar.common.datasource;

import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.AliyunDsConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/6/17 3:54 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliyunDsInstanceConfig extends BaseDsInstanceConfig {

    private AliyunDsConfig.aliyun aliyun;
}
