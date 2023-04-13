package com.baiyi.opscloud.domain.param.sls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/9/5 14:58
 * @Version 1.0
 */
public class SlsParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Hooks {

        List<Alert> alerts;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Alert {
        private String alert_id;
        private String alert_instance_id;
        private String alert_name;
        private Long alert_time;
        private String alert_type;
        private String aliuid;

        private Map<String, String> annotations;

        private List<Map<String, String>> fire_results;

        private Integer fire_results_count;

        private Long fire_time;

        private Map<String, String> labels;

        private Integer next_eval_interval;

        private String project;
        private String region;
        private Integer resolve_time;
        private Integer severity;
        private String status;
    }

}