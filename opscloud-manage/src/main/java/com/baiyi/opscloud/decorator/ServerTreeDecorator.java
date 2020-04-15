package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.ServerFacade;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/8 10:05 上午
 * @Version 1.0
 */
@Component
public class ServerTreeDecorator {

    @Resource
    private ServerFacade serverFacade;

    public TreeVO.Tree decorator(OcServerGroup ocServerGroup, Map<String, List<OcServer>> serverGroupMap) {
        List<TreeVO.Tree> childrens = serverGroupMap.keySet().stream().map(subName -> TreeVO.Tree.builder()
                .id(subName)
                .label(subName)
                .children(getServerChildrens(serverGroupMap.get(subName)))
                .build()).collect(Collectors.toList());

        TreeVO.Tree tree = TreeVO.Tree.builder()
                .id(ocServerGroup.getName())
                .label(ocServerGroup.getName())
                .children(childrens)
                .build();

        return tree;
    }

    private List<TreeVO.Tree> getServerChildrens(List<OcServer> serverList) {
        return serverList.stream().map(this::apply).collect(Collectors.toList());
    }

    private TreeVO.Tree apply(OcServer e) {
        String serverName = serverFacade.acqServerName(e);
        return TreeVO.Tree.builder()
                .id(serverName)
                .label(Joiner.on(":").join(serverName, e.getPrivateIp()))
                .build();
    }
}
