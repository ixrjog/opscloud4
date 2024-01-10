package com.baiyi.opscloud.algorithm;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.service.business.BizPropertyHelper;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.CollectionUtils;

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

    @Resource
    private BizPropertyHelper businessPropertyHelper;

    protected ServerProperty.Server getBusinessProperty(ServerGroup serverGroup) {
        return businessPropertyHelper.getServerGroupProperty(serverGroup.getId());
    }

    /**
     * 按环境分组
     *
     * @param serverGroup
     * @return key: envName
     */
    protected Map<String, List<ServerPack>> groupingByEnv(ServerGroup serverGroup) {
        List<Server> serverList = serverService.queryByServerGroupId(serverGroup.getId());
        return groupingByEnv(serverGroup, serverList);
    }

    protected Map<String, List<ServerPack>> groupingByEnv(ServerGroup serverGroup, int envType) {
        List<Server> serverList = serverService.queryByGroupIdAndEnvType(serverGroup.getId(), envType);
        return groupingByEnv(serverGroup, serverList);
    }

    private Map<String, List<ServerPack>> groupingByEnv(ServerGroup serverGroup, List<Server> serverList) {
        Map<String, List<ServerPack>> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(serverList)) {
            return map;
        }
        List<ServerPack> serverPacks = serverList.stream().map(this::toServerPack).toList();
        serverPacks.forEach(e -> {
            String groupingName = toSubgroupName(serverGroup, e.getEnv());
            if (map.containsKey(groupingName)) {
                map.get(groupingName).add(e);
            } else {
                List<ServerPack> list = Lists.newArrayList();
                list.add(e);
                map.put(groupingName, list);
            }
        });
        return map;
    }

    private ServerPack toServerPack(Server server) {
        return ServerPack.builder()
                .property(businessPropertyHelper.getBusinessProperty(server))
                .env(envService.getByEnvType(server.getEnvType()))
                .server(server)
                .build();
    }

    protected String toSubgroupName(ServerGroup serverGroup, Env env) {
        return Joiner.on("-").join(serverGroup.getName().replace("group_", ""), env.getEnvName());
    }

    public String getHeadInfo() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String headInfoTpl = "# {} on {}\n\n";
        return StringFormatter.arrayFormat(headInfoTpl, Global.CREATED_BY, fastDateFormat.format(new Date()));
    }

}