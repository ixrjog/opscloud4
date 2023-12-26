package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.AbstractZabbixV5ActionDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixAction;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.ConditionsBuilder;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixActionParam;
import com.baiyi.opscloud.zabbix.ZabbixUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/19 4:13 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5ActionDriver extends AbstractZabbixV5ActionDriver {

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_action_name_' + #actionName")
    public void evictActionByName(ZabbixConfig.Zabbix config, String actionName) {
        log.info("Evict cache with Zabbix Action: actionName={}", actionName);
    }

    /**
     * @param actionName Report problems to users_{name}
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_action_name_' + #actionName", unless = "#result == null")
    public ZabbixAction.Action getActionByName(ZabbixConfig.Zabbix config, String actionName) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("output", "extend")
                .putParam("selectOperations", "extend")
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("name", actionName)
                        .build())
                .build();
        ZabbixAction.QueryActionResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public void create(ZabbixConfig.Zabbix config, String actionName, String usergrpName) {
        ZabbixHostGroup.HostGroup hostGroup = zabbixV5HostGroupDatasource.getByName(config, ZabbixUtil.toHostgroupName(usergrpName));
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("name", actionName)
                .putParam("eventsource", 0)
                .putParam("status", 0)
                .putParam("esc_period", "10m")
                // 操作
                .putParam("operations", buildOperations(config, usergrpName))
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("evaltype", 1)
                        .putEntry("conditions", ConditionsBuilder.build(hostGroup.getGroupid()))
                        .build())
                .build();
        ZabbixAction.CreateActionResponse response = createHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getActionids())) {
            log.error("Create Zabbix Action error: actionName={}", actionName);
        }
    }

    private List<ZabbixActionParam.Operation> buildOperations(ZabbixConfig.Zabbix config, String usergrpName) {
        ZabbixUserGroup.UserGroup userGroup = zabbixV5UserGroupDatasource.getByName(config, usergrpName);
        ZabbixActionParam.Opmessage opmessage = ZabbixActionParam.Opmessage.builder()
                .subject(config.getOperation().getSubject())
                .message(config.getOperation().getMessage())
                .build();
        // operations 操作
        ZabbixActionParam.Operation operation = ZabbixActionParam.Operation.builder()
                .opmessage_grp(getOpmessageGrp(userGroup.getUsrgrpid()))
                .opmessage(opmessage)
                .build();
        return Lists.newArrayList(operation);
    }

    private List<Map<String, String>> getOpmessageGrp(String usrgrpid) {
        Map<String, String> usrgrpidMap = Maps.newHashMap();
        usrgrpidMap.put("usrgrpid", usrgrpid);
        return Lists.newArrayList(usrgrpidMap);
    }

    public void delete(ZabbixConfig.Zabbix config, ZabbixAction.Action action) {
        if (action == null) {
            return;
        }
        ZabbixRequest.DeleteRequest request = ZabbixRequest.DeleteRequest.builder()
                .params(new String[]{action.getActionid()})
                .build();
        ZabbixAction.DeleteActionResponse response = deleteHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getActionids())) {
            log.error("Delete Zabbix Action error: actionid={}", action.getActionid());
        }
    }

}