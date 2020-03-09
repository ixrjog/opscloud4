package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.ServerAttributeBuilder;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.config.ServerAttributeConfig;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.config.serverAttribute.ServerAttribute;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.ServerAttributeUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.decorator.ServerAttributeDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;
import com.baiyi.opscloud.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerAttributeService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

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
    public List<OcServerAttributeVO.ServerAttribute> queryServerGroupAttribute(OcServerGroup ocServerGroup) {
        List<OcServerAttribute> serverAttributeList = Lists.newArrayList();
        List<AttributeGroup> attributeGroups = attributeConfig.getGroups();
        for (AttributeGroup ag : attributeGroups) {
            OcServerAttribute ocServerAttribute = new OcServerAttribute();
            ocServerAttribute.setBusinessId(ocServerGroup.getId());
            ocServerAttribute.setBusinessType(BusinessType.SERVERGROUP.getType());
            ocServerAttribute.setGroupName(ag.getName());
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
        }
        return BeanCopierUtils.copyListProperties(serverAttributeList, OcServerAttributeVO.ServerAttribute.class);
    }

    @Override
    public BusinessWrapper<Boolean> saveServerGroupAttribute(OcServerAttributeVO.ServerAttribute serverAttribute) {
        OcServerAttribute preServerAttribute = BeanCopierUtils.copyProperties(serverAttribute, OcServerAttribute.class);
        OcServerAttribute checkServerAttribute = ocServerAttributeService.queryOcServerAttributeByUniqueKey(preServerAttribute);
        if (checkServerAttribute == null) {
            ocServerAttributeService.addOcServerAttribute(preServerAttribute);
        } else {
            preServerAttribute.setId(checkServerAttribute.getId());
            ocServerAttributeService.updateOcServerAttribute(preServerAttribute);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public Map<String, String> getServerGroupAttributeMap(OcServerGroup ocServerGroup) {
        String key = getCacheKey(ocServerGroup.getId());
        Map<String, String> serverGroupAttributeMap = (Map<String, String>) redisUtil.get(key);
        if (serverGroupAttributeMap != null && !serverGroupAttributeMap.isEmpty())
            return serverGroupAttributeMap;
        serverGroupAttributeMap = Maps.newHashMap();
        List<OcServerAttributeVO.ServerAttribute> list = queryServerGroupAttribute(ocServerGroup);
        for (OcServerAttributeVO.ServerAttribute sa : list) {
            AttributeGroup attributeGroup = ServerAttributeUtils.convert(sa.getAttributes());
            serverGroupAttributeMap.putAll(toServerAttributeMap(attributeGroup.getAttributes()));
        }
        redisUtil.set(key, serverGroupAttributeMap, TimeUtils.dayTime * 7);
        //   list.stream().collect(Collectors.toMap(ServerAttribute::getName, a -> a, (k1, k2) -> k1));
        return serverGroupAttributeMap;
    }

    private Map<String, String> toServerAttributeMap(List<ServerAttribute> list) {
        if (list == null || list.isEmpty())
            return Maps.newHashMap();
        return list.stream().collect(Collectors.toMap(ServerAttribute::getName, ServerAttribute::getValue, (k1, k2) -> k1));
    }

    private String getCacheKey(int id) {
        return Joiner.on(":").join("servergroup", "attribute", "server_group_id", id);
    }

    @Override
    public String getManageIp(OcServer ocServer){
        // TODO
      return "";
    }

}
