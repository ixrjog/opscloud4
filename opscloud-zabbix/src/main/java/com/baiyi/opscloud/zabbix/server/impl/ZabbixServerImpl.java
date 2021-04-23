package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.common.util.ZabbixUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.zabbix.api.ActionAPI;
import com.baiyi.opscloud.zabbix.api.ProxyAPI;
import com.baiyi.opscloud.zabbix.api.TemplateAPI;
import com.baiyi.opscloud.zabbix.builder.ConditionsBuilder;
import com.baiyi.opscloud.zabbix.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.entry.*;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.baiyi.opscloud.zabbix.mapper.*;
import com.baiyi.opscloud.zabbix.param.ZabbixActionParam;
import com.baiyi.opscloud.zabbix.param.ZabbixFilter;
import com.baiyi.opscloud.zabbix.param.ZabbixRequestParams;
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

    public static final String ZABBIX_ERROR = "error";

    public static final String ZABBIX_PARENT_TEMPLATES = "parentTemplates";

    public static final String ACTION_NAME_PREFIX = "Report problems to";

    @Resource
    private ZabbixHandler zabbixHandler;

    @Resource
    private ZabbixUserServer zabbixUserServer;

    @Resource
    private ZabbixHostgroupServer zabbixHostgroupServer;

    @Override
    public ZabbixTemplate getTemplate(String name) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("name", name)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(TemplateAPI.GET)
                .paramEntryByFilter(filter)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixTemplateMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ZabbixProxy getProxy(String hostname) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("host", hostname)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(ProxyAPI.GET)
                .paramEntryByFilter(filter)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixProxyMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void deleteAction(String usergrpName) {
        ZabbixAction action = getAction(usergrpName);
        if (action == null) return;
        ZabbixRequestParams request = ZabbixRequestParams.builder()
                .method(ActionAPI.DELETE.getMethod())
                .params(new String[]{action.getActionid()})
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            String actionid = new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("actionids")).get(0);
            log.info("Delete Action ! usergrpName = {}, id = {}", usergrpName, actionid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param usergrpName users_{name}
     * @return
     */
    @Override
    public ZabbixAction getAction(String usergrpName) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("name", buildActionName(usergrpName))
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .paramEntry("output", "extend")
                .paramEntry("selectOperations", "extend")
                .paramEntryByFilter(filter)
                .method(ActionAPI.GET).build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixActionMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception ignored) {
        }
        return null;
    }

    private List<Map<String, String>> getOpmessageGrp(String usrgrpid) {
        Map<String, String> usrgrpidMap = Maps.newHashMap();
        usrgrpidMap.put("usrgrpid", usrgrpid);
        return Lists.newArrayList(usrgrpidMap);
    }

    @Override
    public boolean createAction(String usergrpName) {
        ZabbixAction action = getAction(usergrpName);
        if (action != null) return true;

        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("evaltype", 1)
                .putEntry("conditions", ConditionsBuilder.build(getHostgroup(ZabbixUtils.convertHostgroupName(usergrpName)).getGroupid()))
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .paramEntry("name", buildActionName(usergrpName))
                .paramEntry("eventsource", 0)
                .paramEntry("status", 0)
                .paramEntry("esc_period", "10m")
                .paramEntry("operations", buildOperations(usergrpName)) // 操作
                .paramEntryByFilter(filter)
                .method(ActionAPI.CREATE)
                .build();
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

    private List<ZabbixActionParam.Operation> buildOperations(String usergrpName) {
        // operations 操作
        ZabbixActionParam.Operation operation = ZabbixActionParam.Operation.builder()
                .opmessage_grp(getOpmessageGrp(zabbixUserServer.getUsergroup(usergrpName).getUsrgrpid()))
                .build();
        return Lists.newArrayList(operation);
    }

    private String buildActionName(String usergrpName) {
        return Joiner.on(" ").join(ACTION_NAME_PREFIX, usergrpName);
    }

    private ZabbixHostgroup getHostgroup(String hostgroup) {
        ZabbixHostgroup zabbixHostgroup = zabbixHostgroupServer.getHostgroup(hostgroup);
        if (zabbixHostgroup != null) return zabbixHostgroup;
        return zabbixHostgroupServer.createHostgroup(hostgroup);
    }

    /**
     * @param jsonNode
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> result(JsonNode jsonNode) {
        if (jsonNode != null) {
            try {
                ZabbixError zabbixError = new ZabbixErrorMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_ERROR));
                return new BusinessWrapper<>(70001, zabbixError.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new BusinessWrapper<>(ErrorEnum.ZABBIX_SERVER_ERROR);
    }


}
