package com.baiyi.opscloud.serverAttribute;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.config.ServerAttributeConfig;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.config.serverAttribute.ServerAttribute;
import com.baiyi.opscloud.common.util.ServerAttributeUtils;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.factory.attribute.impl.AnsibleAttribute;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.zabbix.entry.ZabbixMacro;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/6 3:01 下午
 * @Version 1.0
 */
public class ServerAttributeTest extends BaseUnit {

    @Resource
    private ServerAttributeConfig serverAttributeConfig;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Resource
    private OcServerGroupService ocServerGroupService;
    @Resource
    private AnsibleAttribute ansibleAttribute;

    @Resource
    private OcServerService ocServerService;

    @Test
    void testAttributeGroups() {
        List<AttributeGroup> list = serverAttributeConfig.getGroups();
        for (AttributeGroup ag : list)
            System.err.println(JSON.toJSONString(ag));
    }

    @Test
    void testServerAttribute() {
        OcServer ocServer = ocServerService.queryOcServerByIp("172.16.210.240");
        Map<String, String> map = serverAttributeFacade.getServerAttributeMap(ocServer);
        System.err.println(JSON.toJSON(map));
    }


    @Test
    void testYamlJsonParser() {
        List<AttributeGroup> attributeGroups = serverAttributeConfig.getGroups();
        for (AttributeGroup attributeGroup : attributeGroups) {
            Yaml yaml = new Yaml();
            System.err.println("name:" + attributeGroup.getName());
            System.err.println(yaml.dumpAs(attributeGroup, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
        }
    }

    @Test
    void testYamlJsonParser2() {
        String s = "attributes:\n" +
                "- content: 启用公网ip管理，此配置将作用于:zabbix,jumpserver,keybox\n" +
                "  name: global_enable_public_ip_mgmt\n" +
                "  value: 'false'\n" +
                "comment: 全局通用配置\n" +
                "name: global";
        AttributeGroup ag = ServerAttributeUtils.convert(s);
        System.err.println(JSON.toJSONString(ag));
    }

    @Test
    void testGetServerGroupAttribute2() {
        ocServerGroupService.queryAll().forEach(ocServerGroup -> {
            if (ocServerGroup.getGrpType() != 2) return;
            Map<String, String> map = serverAttributeFacade.getServerGroupAttributeMap(ocServerGroup);
            ServerAttributeVO.ServerAttribute serverAttribute = new ServerAttributeVO.ServerAttribute();
            serverAttribute.setBusinessType(BusinessType.SERVERGROUP.getType());
            serverAttribute.setBusinessId(ocServerGroup.getId());
            serverAttribute.setGroupName("zabbix");
            serverAttribute.setComment("Zabbix属性配置项");
            Yaml yaml = new Yaml();
            serverAttribute.setAttributes(yaml.dumpAs(getAttributeGroup(ocServerGroup), Tag.MAP, DumperOptions.FlowStyle.BLOCK));
            //  AttributeGroup ag
            serverAttributeFacade.saveServerAttribute(serverAttribute);
            System.err.println(JSON.toJSONString(map));
        });


    }

    private AttributeGroup getAttributeGroup(OcServerGroup ocServerGroup) {
        AttributeGroup ag = new AttributeGroup();
        ag.setName("zabbix");
        ag.setComment("Zabbix属性配置项");
        List<ServerAttribute> attributes = Lists.newArrayList();

        ServerAttribute attr1 = new ServerAttribute();
        attr1.setContent("双向同步(会清理多余的模版)");
        attr1.setName("zabbix_bidirectional_sync");
        attr1.setValue("false");

        attributes.add(attr1);

        List<ZabbixMacro> macros = Lists.newArrayList();

        ZabbixMacro macro1 = new ZabbixMacro();
        macro1.setMacro("{$PORT}");
        macro1.setValue("8080");
        macros.add(macro1);

        ZabbixMacro macro2 = new ZabbixMacro();
        macro2.setMacro("{$PROJECT}");
        macro2.setValue(ocServerGroup.getName().replace("group_", ""));
        macros.add(macro2);


        ZabbixMacro macro3 = new ZabbixMacro();
        macro3.setMacro("{$WEBSTATUS}");
        macro3.setValue("webStatus");
        macros.add(macro3);

        ServerAttribute attr2 = new ServerAttribute();
        attr2.setContent("'JSONArray格式:[{\"macro\": \"{$USER_ID}\",\"value\": \"1\"} ]'");
        attr2.setName("zabbix_host_macros");
        attr2.setValue(JSON.toJSONString(macros));

        attributes.add(attr2);

        ServerAttribute attr3 = new ServerAttribute();
        attr3.setContent("模版名称（多个用,分割）");
        attr3.setName("zabbix_templates");
        attr3.setValue("Template OS Linux by Zabbix agent,Template Springboot Service");
        attributes.add(attr3);

        ag.setAttributes(attributes);
        return ag;
    }


    @Test
    void testGetServerGroupAttribute() {
        OcServerGroup ocServerGroup = new OcServerGroup();
        ocServerGroup.setId(1);
        List<ServerAttributeVO.ServerAttribute> list = serverAttributeFacade.queryServerGroupAttribute(ocServerGroup);
        System.err.println(JSON.toJSONString(list));
    }

    @Test
    void testGetServerGroupAttributeMap() {
        OcServerGroup ocServerGroup = new OcServerGroup();
        ocServerGroup.setId(1);
        Map<String, String> map = serverAttributeFacade.getServerGroupAttributeMap(ocServerGroup);
        System.err.println(JSON.toJSONString(map));
    }

    @Test
    void testGrouping() {
        // 90
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupByName("group_caesar");
        Map<String, List<OcServer>> map = ansibleAttribute.grouping(ocServerGroup, true);
        System.err.println(JSON.toJSONString(map));
    }


    @Test
    void testGroupingX() {
        // 90
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupByName("group_caesar");
        Map<String, List<OcServer>> map = ansibleAttribute.grouping(ocServerGroup, true);
        HttpResult hr =
                new HttpResult<>(map);
        System.err.println(hr);
    }


    @Test
    void testCache() {
        // 90
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupByName("group_test-springboot2");
        Map<String, List<OcServer>> map = ansibleAttribute.grouping(ocServerGroup, true);
        System.err.println(JSON.toJSONString(map));
    }

}
