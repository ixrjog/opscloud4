package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.ServerDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.OcServerVO;
import com.baiyi.opscloud.facade.ServerFacade;
import com.baiyi.opscloud.service.server.OcServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:47 下午
 * @Version 1.0
 */
@Service
public class ServerFacadeImpl implements ServerFacade {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerDecorator serverDecorator;

    @Override
    public DataTable<OcServerVO.Server> queryServerPage(ServerParam.PageQuery pageQuery) {
        DataTable<OcServer> table = ocServerService.queryOcServerByParam(pageQuery);
        List<OcServerVO.Server> page = BeanCopierUtils.copyListProperties(table.getData(), OcServerVO.Server.class);
        DataTable<OcServerVO.Server> dataTable = new DataTable<>(page.stream().map(e -> serverDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> addServer(OcServerVO.Server server) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateServer(OcServerVO.Server server) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteServerById(int id) {
        return BusinessWrapper.SUCCESS;
    }

}
