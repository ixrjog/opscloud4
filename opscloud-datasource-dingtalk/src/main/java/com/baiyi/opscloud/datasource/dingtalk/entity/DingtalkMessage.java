package com.baiyi.opscloud.datasource.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/12/1 4:48 下午
 * @Version 1.0
 */
public class DingtalkMessage {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageResponse extends DingtalkResponse.Query implements Serializable {
        @Serial
        private static final long serialVersionUID = -2576980035091872379L;
        @JsonProperty("task_id")
        private Long taskId;
    }

}