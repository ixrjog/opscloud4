package com.baiyi.opscloud.datasource.aliyun.ram.entity;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/12/10 2:44 PM
 * @Version 1.0
 */
public class AccessKey {

    @Data
    public static class Key {
        private String accessKeyId;
        private String status;
        private String createDate;
    }

}
