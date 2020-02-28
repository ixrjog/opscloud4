package com.baiyi.opscloud.aliyun.core.config;

import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/28 3:36 下午
 * @Version 1.0
 */
@Data
public class AliyunAccount {

    private String uid;
    private Boolean master;
    private String name;
    private String accessKeyId;
    private String secret;
    private String regionId;
    private List<String> regionIds;
}
