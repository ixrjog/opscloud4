package com.baiyi.opscloud.serverAttribute;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.config.ServerAttributeConfig;
import com.baiyi.opscloud.common.config.serverAttribute.AttributeGroup;
import com.baiyi.opscloud.common.util.ServerAttributeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.ServerAttributeVO;
import com.baiyi.opscloud.factory.attribute.impl.AttributeAnsible;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.server.OcServerGroupService;
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
    private AttributeAnsible attributeAnsible;

    @Test
    void testAttributeGroups() {
        List<AttributeGroup> list = serverAttributeConfig.getGroups();
        for (AttributeGroup ag : list)
            System.err.println(JSON.toJSONString(ag));
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
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(90);
        Map<String, List<OcServer>> map = attributeAnsible.grouping(ocServerGroup, true);
        System.err.println(JSON.toJSONString(map));
    }

}
