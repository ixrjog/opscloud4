package com.baiyi.opscloud.datasource.ansible.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/8/16 11:42 上午
 * @Version 1.0
 */
public class AnsibleVersion {

    public interface VersionType {
        String ANSIBLE = "ANSIBLE";
        String ANSIBLE_PLAYBOOK = "ANSIBLE_PLAYBOOK";
    }

    @Builder
    @Data
    public static class Version {

        private String version; // 版本
        private String executableLocation; // 执行位置
        private String details; // 详情
        private String type;
    }
}
