package com.baiyi.caesar.util;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.packer.server.ServerPacker;
import com.baiyi.caesar.vo.server.ServerTreeVO;
import com.baiyi.caesar.vo.server.ServerVO;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private static ServerPacker serverPacker;

    @Autowired
    public void setServerPacker(ServerPacker serverPacker) {
        ServerTreeUtil.serverPacker = serverPacker;
    }

    public static ServerTreeVO.Tree wrap(ServerGroup serverGroup, Map<String, List<Server>> serverGroupMap) {
        List<ServerTreeVO.Tree> children = serverGroupMap.keySet().stream().map(subName -> ServerTreeVO.Tree.builder()
                .id(subName)
                .label(subName)
                .children(buildServerChildren(serverGroupMap.get(subName)))
                .build()).collect(Collectors.toList());

        return ServerTreeVO.Tree.builder()
                .id(serverGroup.getName())
                .label(serverGroup.getName())
                .children(children)
                .build();
    }

    private static List<ServerTreeVO.Tree> buildServerChildren(List<Server> servers) {
        return servers.stream().map(ServerTreeUtil::apply).collect(Collectors.toList());
    }

    private static ServerTreeVO.Tree apply(Server server) {
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

    private static boolean isDisabled(Server server) {
        if (!server.getIsActive()) return true;
        return "Windows".equalsIgnoreCase(server.getOsType());
    }

    public static void wrap(Map<String, String> serverTreeHostPatternMap, Map<String, List<Server>> serverGroupMap) {
        serverGroupMap.keySet().forEach(k ->
                serverGroupMap.get(k).forEach(s -> serverTreeHostPatternMap.put(ServerUtil.toServerName(s), s.getPrivateIp()))
        );
    }

    public static int getServerGroupMapSize(Map<String, List<Server>> serverGroupMap) {
        if (serverGroupMap.isEmpty())
            return 0;
        int size = 0;
        for (String key : serverGroupMap.keySet())
            size += serverGroupMap.get(key).size();
        return size;
    }
}
