package com.baiyi.caesar.packer.server.util;

import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.vo.tree.TreeVO;
import com.baiyi.caesar.facade.ServerBaseFacade;
import com.google.common.base.Joiner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/25 1:48 下午
 * @Version 1.0
 */
public class ServerTreeUtil {

    public static TreeVO.Tree decorator(OcServerGroup ocServerGroup, Map<String, List<OcServer>> serverGroupMap) {
        List<TreeVO.Tree> childrens = serverGroupMap.keySet().stream().map(subName -> TreeVO.Tree.builder()
                .id(subName)
                .label(subName)
                .children(buildServerChildrens(serverGroupMap.get(subName)))
                .build()).collect(Collectors.toList());

        return TreeVO.Tree.builder()
                .id(ocServerGroup.getName())
                .label(ocServerGroup.getName())
                .children(childrens)
                .build();
    }

    private static List<TreeVO.Tree> buildServerChildrens(List<OcServer> serverList) {
        return serverList.stream().map(ServerTreeUtil::apply).collect(Collectors.toList());
    }

    private static TreeVO.Tree apply(OcServer server) {
        String serverName = ServerBaseFacade.acqServerName(server);
        return TreeVO.Tree.builder()
                .id(serverName)
                .disabled(!server.getIsActive())
                .label(Joiner.on(":").join(serverName, server.getPrivateIp()))
                .build();
    }
}
