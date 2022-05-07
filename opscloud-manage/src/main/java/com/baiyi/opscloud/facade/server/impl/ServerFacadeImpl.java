package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.*;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.factory.resource.base.AbstractApplicationResourceQuery;
import com.baiyi.opscloud.packer.server.ServerPacker;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/24 5:45 下午
 * @Version 1.0
 */
@ApplicationResType(ApplicationResTypeEnum.SERVER)
@BusinessType(BusinessTypeEnum.SERVER)
@Service
@RequiredArgsConstructor
public class ServerFacadeImpl extends AbstractApplicationResourceQuery implements ServerFacade {

    private final ServerService serverService;

    private final ServerPacker serverPacker;

    @Override
    public DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery) {
        DataTable<Server> table = serverService.queryServerPage(pageQuery);
        List<ServerVO.Server> data = BeanCopierUtil.copyListProperties(table.getData(), ServerVO.Server.class).stream().peek(e -> serverPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery) {
        ServerParam.ServerPageQuery query = ServerParam.ServerPageQuery.builder().queryName(pageQuery.getQueryName()).build();
        query.setLength(pageQuery.getLength());
        query.setPage(pageQuery.getPage());
        DataTable<ServerVO.Server> table = queryServerPage(query);
        return new DataTable<>(table.getData().stream().map(e -> ApplicationResourceVO.Resource.builder().name(e.getDisplayName()).applicationId(pageQuery.getApplicationId()).businessId(e.getBusinessId()).resourceType(getApplicationResType()).businessType(getBusinessType()).comment(e.getPrivateIp()).build()).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @EnvWrapper(extend = true, wrapResult = true)
    @AssetBusinessRelation
    public ServerVO.Server addServer(ServerVO.Server server) {
        Server pre = toDO(server);
        serverService.add(pre);
        // 绑定资产
        server.setId(pre.getId());
        return BeanCopierUtil.copyProperties(pre, ServerVO.Server.class);
    }

    @Override
    public void updateServer(ServerVO.Server server) {
        Server pre = toDO(server);
        Server originalServer = serverService.getById(pre.getId());
        if (!originalServer.getSerialNumber().equals(pre.getSerialNumber())) {
            // 判断SN是否重复
            if (serverService.getByUniqueKey(pre.getEnvType(), pre.getSerialNumber(), pre.getServerGroupId()) != null) {
                throw new CommonRuntimeException("更新服务器错误: SerialNumber冲突!");
            }
        }
        try {
            serverService.update(pre);
        } catch (Exception e) {
            throw new CommonRuntimeException("更新服务器错误: 请确认IP、SerialNumber等字段是否有冲突!");
        }
    }

    private Server toDO(ServerVO.Server server) {
        Server pre = BeanCopierUtil.copyProperties(server, Server.class);
        if (IdUtil.isEmpty(pre.getSerialNumber())) {
            Server maxSerialNumberServer = serverService.getMaxSerialNumberServer(pre.getServerGroupId(), pre.getEnvType());
            pre.setSerialNumber(null == maxSerialNumberServer ? 1 : maxSerialNumberServer.getSerialNumber() + 1);
        }
        if (pre.getMonitorStatus() == null) pre.setMonitorStatus(-1);
        if (pre.getServerStatus() == null) pre.setServerStatus(1);
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
        List<ServerVO.Server> data = BeanCopierUtil.copyListProperties(table.getData(), ServerVO.Server.class).stream().peek(e -> serverPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

}
