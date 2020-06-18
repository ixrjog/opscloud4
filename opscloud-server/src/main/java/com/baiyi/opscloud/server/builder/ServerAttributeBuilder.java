package com.baiyi.opscloud.server.builder;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.server.bo.ServerAttributeBO;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * @Author baiyi
 * @Date 2020/3/6 5:53 下午
 * @Version 1.0
 */
public class ServerAttributeBuilder {

    public static OcServerAttribute build(String groupName, int businessType, int businessId) {
        ServerAttributeBO serverAttributeBO = ServerAttributeBO.builder()
                .businessId(businessId)
                .businessType(businessType)
                .groupName(groupName)
                .build();
        return covert(serverAttributeBO);
    }

    public static OcServerAttribute build(AttributeGroup attributeGroup, OcServerGroup ocServerGroup) {
        return build(attributeGroup, BusinessType.SERVERGROUP.getType(), ocServerGroup.getId());
    }

    public static OcServerAttribute build(AttributeGroup attributeGroup, OcServer ocServer) {
        return build(attributeGroup, BusinessType.SERVER.getType(), ocServer.getId());
    }

    public static OcServerAttribute build(int id, AttributeGroup attributeGroup, OcServerGroup ocServerGroup) {
        OcServerAttribute ocServerAttribute = build(attributeGroup, ocServerGroup);
        ocServerAttribute.setId(id);
        return ocServerAttribute;
    }

    public static OcServerAttribute build(int id, AttributeGroup attributeGroup, OcServer ocServer) {
        OcServerAttribute ocServerAttribute = build(attributeGroup, ocServer);
        ocServerAttribute.setId(id);
        return ocServerAttribute;
    }

    private static OcServerAttribute build(AttributeGroup attributeGroup, int businessType, int businessId) {
        Yaml yaml = new Yaml();
        ServerAttributeBO serverAttributeBO = ServerAttributeBO.builder()
                .businessId(businessId)
                .businessType(businessType)
                .groupName(attributeGroup.getName())
                .comment(attributeGroup.getComment())
                .attributes(yaml.dumpAs(attributeGroup, Tag.MAP, DumperOptions.FlowStyle.BLOCK)) // 序列化对象为YAML-BLOCK格式
                .build();
        return covert(serverAttributeBO);
    }


    private static OcServerAttribute covert(ServerAttributeBO serverAttributeBO) {
        return BeanCopierUtils.copyProperties(serverAttributeBO, OcServerAttribute.class);
    }

}
