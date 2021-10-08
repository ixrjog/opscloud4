package com.baiyi.opscloud.util;

import com.baiyi.opscloud.algorithm.ServerPack;
import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.server.ServerPacker;
import com.baiyi.opscloud.service.business.BusinessPropertyHelper;
import com.google.common.base.Joiner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/25 1:48 下午
 * @Version 1.0
 */
@Component
public class ServerTreeUtil {

    @Resource
    private ServerPacker serverPacker;

    @Cacheable(cacheNames = CachingConfig.Repositories.SERVER, key = "'server_tree_severgroupid_' + #serverGroup.id", unless = "#result == null")
    public ServerTreeVO.Tree wrap(ServerGroup serverGroup, Map<String, List<ServerPack>> serverGroupMap) {
        List<ServerTreeVO.Tree> children = serverGroupMap.keySet().stream()
                .map(subName -> ServerTreeVO.Tree.builder()
                        .id(subName)
                        .label(subName)
                        .children(buildServerChildren(serverGroupMap.get(subName)))
                        .build())
                .collect(Collectors.toList());

        return ServerTreeVO.Tree.builder()
                .id(serverGroup.getName())
                .label(serverGroup.getName())
                .children(children)
                .build();
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.SERVER, key = "'server_tree_severgroupid_' + #serverGroupId")
    public void evictWrap(Integer serverGroupId) {
    }

    private List<ServerTreeVO.Tree> buildServerChildren(List<ServerPack> serverPacks) {
        return serverPacks.stream().map(this::apply).collect(Collectors.toList());
    }

    private ServerTreeVO.Tree apply(ServerPack serverPack) {
        String serverName = SimpleServerNameFacade.toName(serverPack.getServer(), serverPack.getEnv());
        ServerVO.Server vo = BeanCopierUtil.copyProperties(serverPack.getServer(), ServerVO.Server.class);
        serverPacker.wrap(vo);
        return ServerTreeVO.Tree.builder()
                .id(serverName)
                .disabled(isDisabled(serverPack.getServer()))
                .server(vo)
                .label(Joiner.on(":").join(serverName, BusinessPropertyHelper.getManageIp(serverPack)))
                .build();
    }

    private boolean isDisabled(Server server) {
        if (!server.getIsActive()) return true;
        return "Windows".equalsIgnoreCase(server.getOsType());
    }

    public void wrap(Map<String, String> serverTreeHostPatternMap, Map<String, List<Server>> serverGroupMap) {
        serverGroupMap.forEach((k, v) ->
                v.forEach(s -> serverTreeHostPatternMap.put(SimpleServerNameFacade.toServerName(s), s.getPrivateIp()))
        );
    }

    public int getServerGroupMapSize(Map<String, List<ServerPack>> serverGroupMap) {
        if (serverGroupMap.isEmpty())
            return 0;
        int size = 0;
        for (String key : serverGroupMap.keySet())
            size += serverGroupMap.get(key).size();
        return size;
    }
}
