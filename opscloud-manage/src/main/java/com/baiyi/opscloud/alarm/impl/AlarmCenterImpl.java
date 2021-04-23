package com.baiyi.opscloud.alarm.impl;


import com.baiyi.opscloud.alarm.AlarmCenter;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeetlUtils;
import com.baiyi.opscloud.dingtalk.DingtalkService;
import com.baiyi.opscloud.dingtalk.builder.DingtalkTemplateBuilder;
import com.baiyi.opscloud.dingtalk.builder.DingtalkTemplateMap;
import com.baiyi.opscloud.dingtalk.content.DingtalkContent;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.facade.EnvFacade;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.facade.UserPermissionFacade;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkService;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.zabbix.base.InterfaceType;
import com.baiyi.opscloud.zabbix.base.SeverityType;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.baiyi.opscloud.zabbix.util.InterfaceUtils;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/2/18 5:02 下午
 * @Version 1.0
 */
@Component
@Slf4j
public class AlarmCenterImpl implements AlarmCenter {

    private static final String FILE_TEMPLATE_ZABBIX_PROBLEM = "ZABBIX_PROBLEM";

    private static final String ZABBIX_PROBLEM_ALARM = "ZABBIX_PROBLEM_ALARM";

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private EnvFacade envFacade;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private OcDingtalkService ocDingtalkService;

    @Resource
    private DingtalkService dingtalkService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void notice(Map<String, List<ZabbixTrigger>> triggerMap) {
        triggerMap.keySet().forEach(k -> {
            List<ZabbixHostInterface> interfaces = zabbixHostServer.getHostInterfaces(k);
            ZabbixHostInterface hostInterface = InterfaceUtils.filter(interfaces, InterfaceType.AGENT);
            if (hostInterface == null) return;

            OcServer ocServer = ocServerService.queryOcServerByIp(hostInterface.getIp());
            if (ocServer == null) return;
//            log.info("serverName = {}, ip = {}", ocServer.getName(), ocServer.getPrivateIp());
            notice(triggerMap.get(k), ocServer);
        });

    }

    private void notice(List<ZabbixTrigger> triggers, OcServer ocServer) {
        int prod = envFacade.convertEnvName(Global.ENV_PROD);
        if (ocServer.getEnvType() != prod) return;
        if (!isSilent(triggers, ocServer)) {
            DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                    .paramEntry("severity", getSeverityName(triggers))
                    .paramEntry("users", getSubscriberUsers(ocServer.getServerGroupId()))
                    .paramEntry("problems", triggers)
                    .paramEntry("host", ServerBaseFacade.acqServerName(ocServer))
                    .paramEntry("ip", ocServer.getPrivateIp())
                    .build();
            String msg = getAlarmTemplate(templateMap);
            if (!Strings.isEmpty(msg)) {
                dingtalkService.doNotify(getDingtalkContent(msg));
            }
        }
    }

    private Boolean isSilent(List<ZabbixTrigger> triggers, OcServer ocServer) {
        String key = Joiner.on("_").join(FILE_TEMPLATE_ZABBIX_PROBLEM, ocServer.getPrivateIp());
        List<String> nowTriggers = triggers.stream().map(ZabbixTrigger::getTriggerid).collect(Collectors.toList());
        if (redisUtil.hasKey(key)) {
            List<String> oldTriggers = (List<String>) redisUtil.get(key);
            if (isSilent(nowTriggers, oldTriggers))
                return true;
        }
        redisUtil.set(key, nowTriggers, 60 * 60 * 2);
        return false;
    }

    private boolean isSilent(List<String> nowTriggers, List<String> oldTriggers) {
        if (nowTriggers.size() == oldTriggers.size())
            return oldTriggers.containsAll(nowTriggers);
        return false;
    }

    private String getAlarmTemplate(DingtalkTemplateMap templateMap) {
        Map<String, Object> contentMap = templateMap.getContents();
        if (contentMap.isEmpty())
            return Strings.EMPTY;
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_ZABBIX_PROBLEM, 0);
        try {
            return BeetlUtils.renderTemplate(template.getContent(), contentMap);
        } catch (IOException e) {
            log.error("渲染ZABBIX PROBLEM告警模板失败", e);
            return Strings.EMPTY;
        }
    }

    private DingtalkContent getDingtalkContent(String msg) {
        OcDingtalk ocDingtalk = ocDingtalkService.queryOcDingtalkByKey(ZABBIX_PROBLEM_ALARM);
        return dingtalkService.getDingtalkContent(ocDingtalk.getDingtalkToken(), msg);
    }

    private String getSeverityName(List<ZabbixTrigger> triggers) {
        ZabbixTrigger trigger = triggers.stream().max(Comparator.comparingInt(t -> Integer.parseInt(t.getPriority()))).get();
        return SeverityType.getName(Integer.parseInt(trigger.getPriority()));
    }

    /**
     * 查询订阅用户
     *
     * @param serverGroupId
     * @return
     */
    private List<OcUser> getSubscriberUsers(int serverGroupId) {
        List<OcUserPermission> permissions = userPermissionFacade.queryPermissions(BusinessType.SERVERGROUP.getType(), serverGroupId);
        return permissions.stream()
                .map(e -> ocUserService.queryOcUserById(e.getUserId()))
                .filter(OcUser::getIsActive)
                .collect(Collectors.toList());
    }

}
