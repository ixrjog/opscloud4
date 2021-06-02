package com.baiyi.caesar.packer.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.packer.sys.EnvPacker;
import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.server.ServerVO;
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

    public List<ServerVO.Server> wrapVOList(List<Server> data) {
        return BeanCopierUtil.copyListProperties(data, ServerVO.Server.class);
    }

    public List<ServerVO.Server> wrapVOList(List<Server> data, IExtend iExtend) {
        List<ServerVO.Server> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    public void wrap(ServerVO.Server server) {
        envPacker.wrap(server);
        tagPacker.wrap(server);
        accountPacker.wrap(server);
    }


}
