package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.*;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.packer.server.ServerPacker;
import com.baiyi.opscloud.service.server.ServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:45 下午
 * @Version 1.0
 */
@AssetType(DsAssetTypeEnum.DEFAULT)
@BusinessType(BusinessTypeEnum.SERVER)
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
    @AssetBusinessRelation // 资产绑定业务对象
    public void addServer(ServerVO.Server server) {
        Server pre = toDO(server);
        serverService.add(pre);
        server.setId(pre.getId()); // 绑定资产
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

    @TagClear
    @BusinessPropertyClear
    @Override
    public void deleteServerById(Integer id) {
        Server server = serverService.getById(id);
        if (server == null) return;
        serverService.delete(server);
    }

    @Override
    public DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery pageQuery) {
        DataTable<Server> table = serverService.queryUserRemoteServerPage(pageQuery);
        return new DataTable<>(serverPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

}
