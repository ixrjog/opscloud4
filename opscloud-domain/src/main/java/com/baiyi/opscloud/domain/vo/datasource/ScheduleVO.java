package com.baiyi.opscloud.domain.vo.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/23 17:52
 * @Version 1.0
 */
public class ScheduleVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job {

        private String name;
        private String group;
        private String status;
        private String description;
        private String cronExpression;

        private List<String> executionTime;

    }

}