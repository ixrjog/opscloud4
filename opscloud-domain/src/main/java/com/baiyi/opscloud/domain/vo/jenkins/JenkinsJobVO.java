package com.baiyi.caesar.domain.vo.jenkins;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/23 4:19 下午
 * @Version 1.0
 */
public class JenkinsJobVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Job {

        private String name;
        private String url;
        private String fullName;

    }
}