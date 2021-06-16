package com.baiyi.caesar.util;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.vo.server.ServerTreeVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.packer.server.ServerPacker;
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

    @Cacheable(cacheNames = CachingConfig.Repositories.SERVER, key = "'serverTreeUtil_wrap_' + #serverGroup.id", unless = "#result == null")
    public ServerTreeVO.Tree wrap(ServerGroup serverGroup, Map<String, List<Server>> serverGroupMap) {
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

    @CacheEvict(cacheNames = CachingConfig.Repositories.SERVER, key = "'serverTreeUtil_wrap_' + #serverGroupId")
    public void evictWrap(Integer serverGroupId) {
    }

    private List<ServerTreeVO.Tree> buildServerChildren(List<Server> servers) {
        return servers.stream().map(this::apply).collect(Collectors.toList());
    }

    private ServerTreeVO.Tree apply(Server server) {
        String serverName = ServerUtil.toServerName(server);
        ServerVO.Server vo = BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
        serverPacker.wrap(vo);
        return ServerTreeVO.Tree.builder()
                .id(serverName)
                .disabled(isDisabled(server))
                .server(vo)
                .label(Joiner.on(":").join(serverName, server.getPrivateIp()))
                .build();
    }

    private boolean isDisabled(Server server) {
        if (!server.getIsActive()) return true;
        return "Windows".equalsIgnoreCase(server.getOsType());
    }

    public void wrap(Map<String, String> serverTreeHostPatternMap, Map<String, List<Server>> serverGroupMap) {
        serverGroupMap.forEach((k, v) ->
                v.forEach(s -> serverTreeHostPatternMap.put(ServerUtil.toServerName(s), s.getPrivateIp()))
        );
    }

    public int getServerGroupMapSize(Map<String, List<Server>> serverGroupMap) {
        if (serverGroupMap.isEmpty())
            return 0;
        int size = 0;
        for (String key : serverGroupMap.keySet())
            size += serverGroupMap.get(key).size();
        return size;
    }
}
