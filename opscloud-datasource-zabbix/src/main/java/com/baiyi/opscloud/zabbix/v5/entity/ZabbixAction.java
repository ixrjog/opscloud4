package com.baiyi.opscloud.zabbix.v5.entity;

import com.baiyi.opscloud.zabbix.v5.entity.base.ZabbixResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 4:16 下午
 * @Version 1.0
 */
public class ZabbixAction {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class QueryActionResponse extends ZabbixResponse.Response {
        private List<Action> result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CreateActionResponse extends ZabbixResponse.Response {
        private Actionids result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class UpdateActionResponse extends CreateActionResponse {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class DeleteActionResponse extends CreateActionResponse {
    }

    @Data
    public static class Actionids {
        private List<String> actionids;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action implements Serializable {
        @Serial
        private static final long serialVersionUID = -8261120829584702330L;
        private String actionid;
        private String name;
        private String eventsource;
        private String status;
    }

}