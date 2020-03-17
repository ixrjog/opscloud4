package com.baiyi.opscloud.aliyun.core.client;

import com.aliyuncs.IAcsClient;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/2/28 5:52 下午
 * @Version 1.0
 */
@Builder
@Data
public class IAcsClientBO {
    private  IAcsClient iAcsClient;
    private AliyunAccount aliyunAccount;
}
