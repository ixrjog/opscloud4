package com.baiyi.opscloud.tencent.cloud.core.config;

import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 4:36 下午
 * @Version 1.0
 */
@Data
public class TencentCloudAccount {

    private String uid;
    private String secretId;
    private String secretKey;
    private String regionId;
    private List<String> zones;

}
