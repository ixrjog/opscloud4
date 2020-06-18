package com.baiyi.opscloud.zabbix.builder;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/3 2:49 下午
 * @Version 1.0
 */
@Data
@Builder
public class ZabbixOperationBO {



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

    private List<Map<String,String>> opmessage_grp;

    @Builder.Default
    private  ZabbixOpmessageBO opmessage = ZabbixOpmessageBO.builder().build();




}
