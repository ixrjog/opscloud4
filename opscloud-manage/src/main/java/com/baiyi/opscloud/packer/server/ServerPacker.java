package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.business.BusinessPropertyPacker;
import com.baiyi.opscloud.packer.sys.EnvPacker;
import com.baiyi.opscloud.packer.tag.TagPacker;
import com.baiyi.opscloud.util.ExtendUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/25 3:41 下午
 * @Version 1.0
 */
@Component
public class ServerPacker {

    @Resource
    private EnvPacker envPacker;

    @Resource
    private TagPacker tagPacker;

    @Resource
    private ServerAccountPacker accountPacker;

    @Resource
    private ServerGroupPacker serverGroupPacker;

    @Resource
    private BusinessPropertyPacker businessPropertyPacker;

    public ServerVO.Server wrapVO(Server data) {
        ServerVO.Server server = BeanCopierUtil.copyProperties(data, ServerVO.Server.class);
        envPacker.wrap(server);
        SimpleServerNameFacade.wrapDisplayName(server);
        return server;
    }

    public List<ServerVO.Server> wrapVOList(List<Server> data) {
        return data.stream().map(this::wrapVO).collect(Collectors.toList());
    }

    public List<ServerVO.Server> wrapVOList(List<Server> data, IExtend iExtend) {
        List<ServerVO.Server> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    public void wrap(ServerVO.Server server) {
        tagPacker.wrap(server);
        accountPacker.wrap(server);
        envPacker.wrap(server);
        serverGroupPacker.wrap(server);
        businessPropertyPacker.wrap(server);
    }
}
