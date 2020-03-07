package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.ServerAttributeBuilder;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.config.ServerAttributeConfig;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.ServerAttributeUtils;
import com.baiyi.opscloud.decorator.ServerAttributeDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.OcServerAttribute;
import com.baiyi.opscloud.domain.generator.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.OcServerAttributeVO;
import com.baiyi.opscloud.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerAttributeService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/7 10:28 上午
 * @Version 1.0
 */
@Service
public class ServerAttributeFacadeImpl implements ServerAttributeFacade {

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
                AttributeGroup attributeGroup = ServerAttributeUtils.convert(preServerAttribute.getAttributes());
                serverAttributeList.add(ServerAttributeBuilder.build(preServerAttribute.getId(), ServerAttributeDecorator.decorator(ag, attributeGroup), ocServerGroup));
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

}
