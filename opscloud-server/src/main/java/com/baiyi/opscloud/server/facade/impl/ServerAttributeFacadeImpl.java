package com.baiyi.opscloud.server.facade.impl;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.config.ServerAttributeConfig;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.config.serverAttribute.ServerAttribute;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.ServerAttributeUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.server.builder.ServerAttributeBuilder;
import com.baiyi.opscloud.server.decorator.ServerAttributeDecorator;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerAttributeService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:28 上午
 * @Version 1.0
 */
@Service
public class ServerAttributeFacadeImpl implements ServerAttributeFacade {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ServerAttributeConfig attributeConfig;

    @Resource
    private OcServerAttributeService ocServerAttributeService;

    @Override
    public List<ServerAttributeVO.ServerAttribute> queryServerGroupAttribute(OcServerGroup ocServerGroup) {
        return BeanCopierUtils.copyListProperties(getServerGroupAttribute(ocServerGroup), ServerAttributeVO.ServerAttribute.class);
    }

    public List<OcServerAttribute> getServerAttribute(OcServer ocServer) {
        List<OcServerAttribute> serverAttributeList = Lists.newArrayList();
        // 服务器组属性
        getServerGroupAttribute(getOcServerGroup(ocServer)).forEach(e -> {
            // serverGroup的属性配置
            AttributeGroup ag = ServerAttributeUtils.convert(e.getAttributes());
            OcServerAttribute ocServerAttribute = ServerAttributeBuilder.build(ag.getName(), BusinessType.SERVER.getType(), ocServer.getId());
            OcServerAttribute preServerAttribute = ocServerAttributeService.queryOcServerAttributeByUniqueKey(ocServerAttribute);
            if (preServerAttribute == null) {
                // 从配置获取
                serverAttributeList.add(ServerAttributeBuilder.build(ag, ocServer));
            } else {
                // db中的配置项
                try {
                    AttributeGroup attributeGroup = ServerAttributeUtils.convert(preServerAttribute.getAttributes());
                    serverAttributeList.add(ServerAttributeBuilder.build(preServerAttribute.getId(), ServerAttributeDecorator.decorator(ag, attributeGroup), ocServer));
                } catch (Exception ex) {
                    // 数据格式错误，删除数据后生成默认配置项
                    ocServerAttributeService.deleteOcServerAttributeById(preServerAttribute.getId());
                    serverAttributeList.add(ServerAttributeBuilder.build(ag, ocServer));
                }
            }
        });
        return serverAttributeList;
    }

    private OcServerGroup getOcServerGroup(OcServer ocServer) {
        OcServerGroup ocServerGroup = new OcServerGroup();
        ocServerGroup.setId(ocServer.getServerGroupId());
        return ocServerGroup;
    }

    public List<OcServerAttribute> getServerGroupAttribute(OcServerGroup ocServerGroup) {
        List<OcServerAttribute> serverAttributeList = Lists.newArrayList();
        attributeConfig.getGroups().forEach(ag -> {
            OcServerAttribute ocServerAttribute = ServerAttributeBuilder.build(ag.getName(), BusinessType.SERVERGROUP.getType(), ocServerGroup.getId());
            OcServerAttribute preServerAttribute = ocServerAttributeService.queryOcServerAttributeByUniqueKey(ocServerAttribute);
            if (preServerAttribute == null) {
                // 从配置获取
                serverAttributeList.add(ServerAttributeBuilder.build(ag, ocServerGroup));
            } else {
                // db中的配置项
                try {
                    AttributeGroup attributeGroup = ServerAttributeUtils.convert(preServerAttribute.getAttributes());
                    serverAttributeList.add(ServerAttributeBuilder.build(preServerAttribute.getId(), ServerAttributeDecorator.decorator(ag, attributeGroup), ocServerGroup));
                } catch (Exception e) {
                    // 数据格式错误，删除数据后生成默认配置项
                    ocServerAttributeService.deleteOcServerAttributeById(preServerAttribute.getId());
                    serverAttributeList.add(ServerAttributeBuilder.build(ag, ocServerGroup));
                }
            }
        });
        return serverAttributeList;
    }

    @Override
    public List<ServerAttributeVO.ServerAttribute> queryServerAttribute(OcServer ocServer) {
        return BeanCopierUtils.copyListProperties(getServerAttribute(ocServer), ServerAttributeVO.ServerAttribute.class);
    }

    @Override
    public List<OcServerAttribute> queryServerAttributeById(int serverId) {
        return ocServerAttributeService.queryOcServerAttributeByBusinessTypeAndBusinessId(BusinessType.SERVER.getType(), serverId);
    }

    @Override
    public void deleteServerAttributeByList(List<OcServerAttribute> serverAttributeList) {
        serverAttributeList.forEach(e -> ocServerAttributeService.deleteOcServerAttributeById(e.getId()));
    }

    @Override
    public BusinessWrapper<Boolean> saveServerAttribute(ServerAttributeVO.ServerAttribute serverAttribute) {
        OcServerAttribute preServerAttribute = BeanCopierUtils.copyProperties(serverAttribute, OcServerAttribute.class);
        OcServerAttribute checkServerAttribute = ocServerAttributeService.queryOcServerAttributeByUniqueKey(preServerAttribute);
        if (checkServerAttribute == null) {
            ocServerAttributeService.addOcServerAttribute(preServerAttribute);
        } else {
            preServerAttribute.setId(checkServerAttribute.getId());
            ocServerAttributeService.updateOcServerAttribute(preServerAttribute);
        }
        if (serverAttribute.getBusinessType() == BusinessType.SERVER.getType()) {
            redisUtil.del(getServerCacheKey(serverAttribute.getBusinessId()));
        }
        if (serverAttribute.getBusinessType() == BusinessType.SERVERGROUP.getType()) {
            redisUtil.del(getServerGroupCacheKey(serverAttribute.getBusinessId()));
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public Map<String, String> getServerGroupAttributeMap(OcServerGroup ocServerGroup) {
        String key = getServerGroupCacheKey(ocServerGroup.getId());
        Map<String, String> serverGroupAttributeMap = (Map<String, String>) redisUtil.get(key);
        if (serverGroupAttributeMap != null && !serverGroupAttributeMap.isEmpty())
            return serverGroupAttributeMap;
        serverGroupAttributeMap = Maps.newHashMap();
        List<ServerAttributeVO.ServerAttribute> list = queryServerGroupAttribute(ocServerGroup);
        for (ServerAttributeVO.ServerAttribute sa : list) {
            AttributeGroup attributeGroup = ServerAttributeUtils.convert(sa.getAttributes());
            serverGroupAttributeMap.putAll(convertServerAttributeMap(attributeGroup.getAttributes()));
        }
        redisUtil.set(key, serverGroupAttributeMap, 60 * 60 * 24 * 7);
        return serverGroupAttributeMap;
    }

    @Override
    public Map<String, String> getServerAttributeMap(OcServer ocServer) {
        String key = getServerCacheKey(ocServer.getId());
        Map<String, String> serverAttributeMap = (Map<String, String>) redisUtil.get(key);
        if (serverAttributeMap != null && !serverAttributeMap.isEmpty())
            return serverAttributeMap;
        serverAttributeMap = Maps.newHashMap();
        List<ServerAttributeVO.ServerAttribute> list = queryServerAttribute(ocServer);
        for (ServerAttributeVO.ServerAttribute sa : list) {
            AttributeGroup attributeGroup = ServerAttributeUtils.convert(sa.getAttributes());
            serverAttributeMap.putAll(convertServerAttributeMap(attributeGroup.getAttributes()));
        }
        redisUtil.set(key, serverAttributeMap, 60 * 60 * 24 * 7);
        return serverAttributeMap;
    }


    private Map<String, String> convertServerAttributeMap(List<ServerAttribute> list) {
        if (list == null || list.isEmpty())
            return Maps.newHashMap();
        return list.stream().collect(Collectors.toMap(ServerAttribute::getName, ServerAttribute::getValue, (k1, k2) -> k1));
    }

    private String getServerGroupCacheKey(int id) {
        return Joiner.on(":").join("servergroup", "attribute", "server_group_id", id);
    }

    private String getServerCacheKey(int id) {
        return Joiner.on(":").join("server", "attribute", "server_id", id);
    }

    @Override
    public String getManageIp(OcServer ocServer) {
        Map<String, String> map = getServerAttributeMap(ocServer);
        if (map == null)
            return ocServer.getPrivateIp();
        if (!map.containsKey(Global.SERVER_ATTRIBUTE_GLOBAL_ENABLE_PUBLIC_IP_MGMT))
            return ocServer.getPrivateIp();
        String value = map.get(Global.SERVER_ATTRIBUTE_GLOBAL_ENABLE_PUBLIC_IP_MGMT);
        if (value.equalsIgnoreCase("true")) {
            if (!StringUtils.isEmpty(ocServer.getPublicIp()))
                return ocServer.getPublicIp();
        }
        return ocServer.getPrivateIp();
    }

    @Override
    public String getSSHPort(OcServer ocServer) {
        Map<String, String> map = getServerAttributeMap(ocServer);
        if (map == null)
            return "22";
        if (map.containsKey(Global.SERVER_ATTRIBUTE_GLOBAL_SSH_PORT))
            return map.get(Global.SERVER_ATTRIBUTE_GLOBAL_SSH_PORT);
        return "22";
    }

}
