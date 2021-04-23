package com.baiyi.opscloud.zabbix.param;

import com.baiyi.opscloud.zabbix.config.ZabbixConfig;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/1 10:36 上午
 * @Version 1.0
 */
@Component
public class ZabbixActionParam {

    private static ZabbixConfig zabbixConfig;

    @Resource
    public void setZabbixConfig(ZabbixConfig zabbixConfig) {
        ZabbixActionParam.zabbixConfig = zabbixConfig;
    }

    @Data
    @Builder
    public static class Condition {

        private Integer conditiontype;
        private Integer operator;
        private String value;
        // 26 - event tag value.
        // Secondary value to compare with. Required for trigger actions when condition type is 26.
        @Builder.Default
        private String value2 = "";
        private String formulaid;

    }

    @Data
    @Builder
    public static class Operation {

        @Builder.Default
        private Integer operationtype = 0;
        // 4.0 版本修改   operation.put("esc_period", 0);
        @Builder.Default
        private String esc_period = "0s";
        @Builder.Default
        private Integer esc_step_from = 1;
        @Builder.Default
        private Integer esc_step_to = 1;

        @Builder.Default
        private Integer evaltype = 0;

        private List<Map<String, String>> opmessage_grp;

        @Builder.Default
        private Opmessage opmessage = Opmessage.builder().build();

    }

    @Data
    @Builder
    public static class Opmessage {

        @Builder.Default
        private Integer mediatypeid = 0;

        /**
         * Whether to use the default action message text and subject.
         * <p>
         * Possible values:
         * 0 - use the data from the operation;
         * 1 - (default) use the data from the media type.
         */
        @Builder.Default
        private Integer default_msg = 0;

        @Builder.Default
        private String subject = zabbixConfig.getOperation().getSubject();

        @Builder.Default
        private String message = zabbixConfig.getOperation().getMessage();

    }

}
