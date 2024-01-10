package com.baiyi.opscloud.datasource.gitlab.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gitlab4j.api.models.SshKey;

/**
 * @Author baiyi
 * @Date 2022/10/27 16:11
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class GitLabSshKey extends SshKey {

    private String username;

}