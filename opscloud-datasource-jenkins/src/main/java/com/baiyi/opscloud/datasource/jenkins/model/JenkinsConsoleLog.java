package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/11/29 18:40
 * @Version 1.0
 */
public class JenkinsConsoleLog {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Log {

        private Integer buildId;
        private String log;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}