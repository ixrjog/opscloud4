package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.common.util.ZabbixUtils;
import com.baiyi.opscloud.zabbix.builder.ZabbixConditionBO;
import com.baiyi.opscloud.zabbix.builder.ZabbixOperationBO;
import com.baiyi.opscloud.zabbix.entry.ZabbixAction;
import com.baiyi.opscloud.zabbix.entry.ZabbixProxy;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixActionMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixProxyMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixTemplateMapper;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.baiyi.opscloud.zabbix.server.ZabbixUserServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2019/12/31 5:56 下午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixServer")
public class ZabbixServerImpl implements com.baiyi.opscloud.zabbix.server.ZabbixServer {

    public static final String ZABBIX_RESULT = "result";
    public static final String ZABBIX_PARENT_TEMPLATES = "parentTemplates";

    @Resource
    private ZabbixHandler zabbixHandler;

    @Resource
    private ZabbixUserServer zabbixUserServer;

    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Override
    public ZabbixTemplate getTemplate(String name) {
        Map<String, String> filter = Maps.newHashMap();
        filter.put("name", name);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("template.get")
                .paramEntry("filter", filter)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixTemplateMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public ZabbixProxy getProxy(String hostname) {
        Map<String, String> filter = Maps.newHashMap();
        filter.put("host", hostname);
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("proxy.get")
                .paramEntry("filter", filter)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixProxyMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param usergrpName users_{name}
     * @return
     */
    @Override
    public ZabbixAction getAction(String usergrpName) {
        Map<String, String> filter = Maps.newHashMap();
        filter.put("name", Joiner.on(" ").join("Report problems to", usergrpName));
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .paramEntry("output", "extend")
                .paramEntry("selectOperations", "extend")
                .paramEntry("filter", filter)
                .method("action.get").build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixActionMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
        }
        return null;
    }

//    /**
//     * 转换用户组名称
//     *
//     * @param serverGroupName
//     * @return
//     */
//    private String convertUsergrpName(String serverGroupName) {
//        return serverGroupName.replace("group_", "users_");
//    }

    private List<Map<String, String>> getOpmessageGrp(String usrgrpid) {
        List<Map<String, String>> opmessageGrp = Lists.newArrayList();
        Map<String, String> usrgrpidMap = Maps.newHashMap();
        usrgrpidMap.put("usrgrpid", usrgrpid);
        opmessageGrp.add(usrgrpidMap);
        return opmessageGrp;
    }

    private List<ZabbixConditionBO> getConditions(String groupid) {
        List<ZabbixConditionBO> conditions = Lists.newArrayList();

        /**
         * https://www.zabbix.com/documentation/4.0/manual/api/reference/action/object?s[]=conditions
         * 	触发器示警度
         */
        ZabbixConditionBO conditionA = ZabbixConditionBO.builder()
                .conditiontype(4)
                .operator(5)
                .value("4")
                .formulaid("A")
                .build();
        conditions.add(conditionA);
        // 服务器组
        ZabbixConditionBO conditionB = ZabbixConditionBO.builder()
                .conditiontype(0)
                .operator(0)
                .value(groupid)
                .formulaid("B")
                .build();
        conditions.add(conditionB);
        return conditions;
    }

    @Override
    public boolean createAction(String usergrpName) {
        ZabbixAction action = getAction(usergrpName);
        if (action != null) return true;
        // operations 操作
        ZabbixOperationBO operation = ZabbixOperationBO.builder()
                .opmessage_grp(getOpmessageGrp(zabbixUserServer.getUsergroup(usergrpName).getUsrgrpid()))
                .build();
        List<ZabbixOperationBO> operations = Lists.newArrayList();
        operations.add(operation);

        List<ZabbixConditionBO> conditions = getConditions(zabbixHostgroupServer.getHostgroup(ZabbixUtils.convertHostgroupName(usergrpName)).getGroupid());

        Map<String, Object> filter = Maps.newHashMap();
        filter.put("evaltype", 1);
        filter.put("conditions", conditions);

        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .paramEntry("name", "Report problems to " + usergrpName)
                .paramEntry("eventsource", 0)
                .paramEntry("status", 0)
                .paramEntry("esc_period", "10m")
                .paramEntry("def_shortdata", "{TRIGGER.NAME}: {TRIGGER.STATUS}")
                .paramEntry("def_longdata", "ServerName:{HOST.NAME} IP:{HOST.IP}\r\n"
                        + "Trigger: {TRIGGER.NAME}\r\n"
                        + "Trigger severity: {TRIGGER.SEVERITY}\r\n"
                        + "Item values: {ITEM.NAME1} ({HOST.NAME1}:{ITEM.KEY1}): {ITEM.VALUE1}\r\n"
                        + "Original event ID: {EVENT.ID}\r\n"
                        + "Trigger time:{EVENT.DATE} {EVENT.TIME}")
                .paramEntry("operations", operations)
                .paramEntry("filter", filter)
                .method("action.create").build();

        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String actionid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("actionids")).get(0);
            if (!StringUtils.isEmpty(actionid))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


}
