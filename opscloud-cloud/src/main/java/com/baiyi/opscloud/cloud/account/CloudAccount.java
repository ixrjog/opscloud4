package com.baiyi.opscloud.cloud.account;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/2/29 1:52 下午
 * @Version 1.0
 */
@Data
public class CloudAccount {

    private String uid;
    private Boolean master = true;
    private String name;
    private String regionId;
}
