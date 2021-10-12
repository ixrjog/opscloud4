package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsTencentExmailConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:07 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TencentExmailDsInstanceConfig extends BaseDsInstanceConfig {

    private DsTencentExmailConfig.Tencent tencent;

}

