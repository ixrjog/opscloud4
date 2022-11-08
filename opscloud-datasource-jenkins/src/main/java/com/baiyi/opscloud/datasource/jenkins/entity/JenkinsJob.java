package com.baiyi.opscloud.datasource.jenkins.entity;

import com.baiyi.opscloud.datasource.jenkins.model.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/10/31 20:36
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JenkinsJob extends Job {

    private Boolean isFolder;
    private Boolean isTemplate;

}
