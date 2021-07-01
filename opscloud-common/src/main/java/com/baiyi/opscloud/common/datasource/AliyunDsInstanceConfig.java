package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
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

    private DsAliyunConfig.Aliyun aliyun;
}
