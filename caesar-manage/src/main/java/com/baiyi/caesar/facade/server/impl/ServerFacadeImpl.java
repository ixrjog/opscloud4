package com.baiyi.caesar.facade.server.impl;

import com.baiyi.caesar.common.annotation.TagClear;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.param.server.ServerParam;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.facade.server.ServerFacade;
import com.baiyi.caesar.packer.server.ServerPacker;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:45 下午
 * @Version 1.0
 */
@Service
public class ServerFacadeImpl implements ServerFacade {

    @Resource
    private ServerService serverService;

    @Resource
    private ServerPacker serverPacker;

    @Override
    public DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery) {
        DataTable<Server> table = serverService.queryServerPage(pageQuery);
        return new DataTable<>(serverPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addServer(ServerVO.Server server) {
        Server pre = toDO(server);
        serverService.add(pre);
    }

    @Override
    public void updateServer(ServerVO.Server server) {
        Server pre = toDO(server);
        serverService.update(pre);
    }

    private Server toDO(ServerVO.Server server) {
        Server pre = BeanCopierUtil.copyProperties(server, Server.class);
        if (IdUtil.isEmpty(pre.getSerialNumber())) {
            Server maxSerialNumberServer = serverService.getMaxSerialNumberServer(pre.getServerGroupId(), pre.getEnvType());
            pre.setSerialNumber(null == maxSerialNumberServer ? 1 : maxSerialNumberServer.getSerialNumber() + 1);
        }
        return pre;
    }

    @TagClear(type = BusinessTypeEnum.SERVER)
    @Override
    public void deleteServerById(Integer id) {

    }

}
