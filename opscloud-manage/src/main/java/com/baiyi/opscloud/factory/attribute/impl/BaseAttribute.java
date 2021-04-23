package com.baiyi.opscloud.factory.attribute.impl;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/7 11:44 上午
 * @Version 1.0
 */
@Component
public abstract class BaseAttribute {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcEnvService ocEnvService;

    /**
     * 按环境分组
     *
     * @param ocServerGroup
     * @return key: envName
     */
    protected Map<String, List<OcServer>> groupingByEnv(OcServerGroup ocServerGroup) {
        List<OcServer> serverList = ocServerService.queryOcServerByServerGroupId(ocServerGroup.getId());
        return groupingByEnv(ocServerGroup, serverList);
    }

    protected Map<String, List<OcServer>> groupingByEnv(OcServerGroup ocServerGroup, int envType) {
        List<OcServer> serverList = ocServerService.queryOcServerByServerGroupIdAndEnvType(ocServerGroup.getId(), envType);
        return groupingByEnv(ocServerGroup, serverList);
    }

    protected Map<String, List<OcServer>> groupingByEnv(OcServerGroup ocServerGroup, List<OcServer> serverList) {
        Map<String, List<OcServer>> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(serverList)) return map;
        serverList.forEach(e -> {
            String groupingName = convertSubgroupName(ocServerGroup, e.getEnvType());
            if (map.containsKey(groupingName)) {
                map.get(groupingName).add(e);
            } else {
                List<OcServer> list = Lists.newArrayList();
                list.add(e);
                map.put(groupingName, list);
            }
        });
        return map;
    }

    /**
     * ddd-prod
     *
     * @param ocServerGroup
     * @param envType
     * @return
     */
    protected String convertSubgroupName(OcServerGroup ocServerGroup, int envType) {
        return Joiner.on("-").join(ocServerGroup.getName().replace("group_", ""), getEnvName(envType));
    }

    public String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return Joiner.on(" ").join("#", Global.CREATED_BY, "on", fastDateFormat.format(new Date()), "\n\n");
    }

    protected String getEnvName(int envType) {
        return ocEnvService.queryOcEnvByType(envType).getEnvName();
    }

}
