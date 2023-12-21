package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.manager.ZabbixInstanceManager;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.*;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.application.ApplicationResourceParam;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.server.converter.ServerConverter;
import com.baiyi.opscloud.facade.server.ServerFacade;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.factory.resource.base.AbstractAppResQuery;
import com.baiyi.opscloud.packer.server.ServerPacker;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
public class ServerFacadeImpl extends AbstractAppResQuery implements ServerFacade {

    private final ServerService serverService;

    private final ServerPacker serverPacker;

    private final ZabbixInstanceManager zabbixInstanceManager;

    private final ServerConverter serverConverter;

    @Override
    public DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery) {
        DataTable<Server> table = serverService.queryServerPage(pageQuery);
        List<ServerVO.Server> data = BeanCopierUtil.copyListProperties(table.getData(), ServerVO.Server.class).stream()
                .peek(e -> serverPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery) {
        ServerParam.ServerPageQuery query = ServerParam.ServerPageQuery.builder().queryName(pageQuery.getQueryName()).build();
        query.setLength(pageQuery.getLength());
        query.setPage(pageQuery.getPage());
        DataTable<ServerVO.Server> table = queryServerPage(query);
        return new DataTable<>(table.getData().stream()
                .map(e -> ApplicationResourceVO.Resource.builder()
                        .name(e.getDisplayName())
                        .applicationId(pageQuery.getApplicationId())
                        .businessId(e.getBusinessId())
                        .resourceType(getAppResType())
                        .businessType(getBusinessType())
                        .comment(e.getPrivateIp())
                        .build())
                .collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @EnvWrapper(extend = true, wrapResult = true)
    @AssetBusinessRelation
    public ServerVO.Server addServer(ServerParam.AddServer addServer) {
        Server server = serverConverter.to(addServer);
        server.setDisplayName(SimpleServerNameFacade.toServerName(server));
        serverService.add(server);
        // 绑定资产
        addServer.setId(server.getId());
        return BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
    }

    @Override
    public void updateServer(ServerParam.UpdateServer updateServer) {
        Server server = serverConverter.to(updateServer);
        server.setDisplayName(SimpleServerNameFacade.toServerName(server));
        serverService.update(server);
    }

    @Override
    @Async
    public void scanServerMonitoringStatus() {
        List<Server> servers = serverService.selectAll();
        zabbixInstanceManager.updateServerMonitorStatus(servers);
    }

    @TagClear
    @BusinessObjectClear
    @Override
    public void deleteServerById(Integer id) {
        Server server = serverService.getById(id);
        if (server != null) {
            serverService.delete(server);
        }
    }

    @Override
    public DataTable<ServerVO.Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery pageQuery) {
        DataTable<Server> table = serverService.queryUserRemoteServerPage(pageQuery);
        List<ServerVO.Server> data = BeanCopierUtil.copyListProperties(table.getData(), ServerVO.Server.class)
                .stream()
                .peek(e -> serverPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

}