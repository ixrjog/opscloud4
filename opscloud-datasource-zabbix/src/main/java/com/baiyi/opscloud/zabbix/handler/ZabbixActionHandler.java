package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixAction;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.param.ConditionsBuilder;
import com.baiyi.opscloud.zabbix.param.ZabbixActionParam;
import com.baiyi.opscloud.zabbix.util.ZabbixUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author baiyi
 * @Date 2021/8/24 4:31 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixActionHandler extends BaseZabbixHandler<ZabbixAction> {

    public static final String ACTION_NAME_PREFIX = "Report problems to";

    @Resource
    private ZabbixUserGroupHandler zabbixUserGroupHandler;

    @Resource
    private ZabbixHostGroupHandler zabbixHostGroupHandler;

    public interface ActionAPIMethod {
        String GET = "action.get";
        String CREATE = "action.create";
        String DELETE = "action.delete";
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + 'action_name_' + #actionName")
    public void evictActionByName(DsZabbixConfig.Zabbix zabbix, String actionName) {
        log.info("清除ZabbixAction缓存 : name = {}", actionName);
    }

    /**
     * @param actionName Report problems to users_{name}
     * @return
     */
    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + 'action_name_' + #actionName", unless = "#result == null")
    public ZabbixAction getActionByName(DsZabbixConfig.Zabbix zabbix, String actionName) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("name", actionName)
                .build();

        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ActionAPIMethod.GET)
                .paramEntry("output", "extend")
                .paramEntry("selectOperations", "extend")
                .filter(filter)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixAction.class);
    }

    public void create(DsZabbixConfig.Zabbix zabbix, String actionName, String usergrpName) {
        ZabbixHostGroup zabbixHostGroup = zabbixHostGroupHandler.getByName(zabbix, ZabbixUtil.toHostgroupName(usergrpName));
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("evaltype", 1)
                .putEntry("conditions", ConditionsBuilder.build(zabbixHostGroup.getGroupid()))
                .build();

        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .paramEntry("name", actionName)
                .paramEntry("eventsource", 0)
                .paramEntry("status", 0)
                .paramEntry("esc_period", "10m")
                .paramEntry("operations", buildOperations(zabbix, usergrpName)) // 操作
                .filter(filter)
                .method(ActionAPIMethod.CREATE)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("actionids").isEmpty()) {
            log.error("创建ZabbixAction失败: name = {}", actionName);
        } else {
            log.info("创建ZabbixAction: name = {}", actionName);
        }
    }

    private List<ZabbixActionParam.Operation> buildOperations(DsZabbixConfig.Zabbix zabbix, String usergrpName) {
        ZabbixUserGroup zabbixUserGroup = zabbixUserGroupHandler.getByName(zabbix, usergrpName);
        ZabbixActionParam.Opmessage opmessage = ZabbixActionParam.Opmessage.builder()
                .subject(zabbix.getOperation().getSubject())
                .message(zabbix.getOperation().getMessage())
                .build();
        // operations 操作
        ZabbixActionParam.Operation operation = ZabbixActionParam.Operation.builder()
                .opmessage_grp(getOpmessageGrp(zabbixUserGroup.getUsrgrpid()))
                .opmessage(opmessage)
                .build();
        return Lists.newArrayList(operation);
    }

    private List<Map<String, String>> getOpmessageGrp(String usrgrpid) {
        Map<String, String> usrgrpidMap = Maps.newHashMap();
        usrgrpidMap.put("usrgrpid", usrgrpid);
        return Lists.newArrayList(usrgrpidMap);
    }


    public void delete(DsZabbixConfig.Zabbix zabbix, ZabbixAction zabbixAction) {
        if (zabbixAction == null) return;
        ZabbixDeleteRequest request = ZabbixDeleteRequest.builder()
                .method(ActionAPIMethod.DELETE)
                .params(new String[]{zabbixAction.getActionid()})
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("actionids").isEmpty()) {
            log.error("删除ZabbixAction失败: actionid = {}", zabbixAction.getActionid());
        } else {
            log.info("删除ZabbixAction: actionid = {}", zabbixAction.getActionid());
        }
    }


    public String buildActionName(String usergrpName) {
        return Joiner.on(" ").join(ACTION_NAME_PREFIX, usergrpName);
    }
}
