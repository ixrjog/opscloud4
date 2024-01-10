package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.domain.base.IRecover;
import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 4:40 下午
 * @Version 1.0
 */
public class ZabbixProblem {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryProblemResponse extends ZabbixResponse.Response {
        private List<Problem> result;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Problem implements IRecover, Serializable {
        @Serial
        private static final long serialVersionUID = 6687178758774377921L;
        private String eventid;
        private String name;
        private Integer object;
        private String objectid;
        // 问题事件创建的时间
        private Long clock;
        /**
         * 问题当前级别
         * 0-未定义
         * 1-信息
         * 2-警告
         * 3-一般严重
         * 4-严重
         * 5-灾难
         */
        private Integer severity;
        /**
         * 事件恢复的UNIT时间戳
         */
        @JsonProperty("r_clock")
        private Long rClock;

        @Override
        public boolean isRecover() {
            return rClock != 0;
        }
    }

}