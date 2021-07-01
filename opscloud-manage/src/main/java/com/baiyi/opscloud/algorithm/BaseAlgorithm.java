package com.baiyi.opscloud.algorithm;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/28 1:35 下午
 * @Version 1.0
 */
public abstract class BaseAlgorithm {

    @Resource
    private ServerService serverService;

    @Resource
    private EnvService envService;

    /**
     * 按环境分组
     *
     * @param serverGroup
     * @return key: envName
     */
    protected Map<String, List<Server>> groupingByEnv(ServerGroup serverGroup) {
        List<Server> serverList = serverService.queryByServerGroupId(serverGroup.getId());
        Map<String, List<Server>> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(serverList)) return map;
        serverList.forEach(e -> {
            String groupingName = toSubgroupName(serverGroup, e.getEnvType());
            if (map.containsKey(groupingName)) {
                map.get(groupingName).add(e);
            } else {
                List<Server> list = Lists.newArrayList();
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
    protected String toSubgroupName(ServerGroup ocServerGroup, int envType) {
        return Joiner.on("-").join(ocServerGroup.getName().replace("group_", ""), getEnvName(envType));
    }

    public String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return Joiner.on(" ").join("#", Global.CREATED_BY, "on", fastDateFormat.format(new Date()), "\n\n");
    }

    protected String getEnvName(int envType) {
        return envService.getByEnvType(envType).getEnvName();
    }

}
