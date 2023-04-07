package com.baiyi.opscloud.domain.vo.jenkins;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema
    public static class Job {

        private String name;
        private String url;
        private String fullName;

    }

}