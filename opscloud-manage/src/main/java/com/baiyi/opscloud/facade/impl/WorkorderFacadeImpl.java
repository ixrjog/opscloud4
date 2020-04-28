package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.WorkorderGroupDecorator;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;
import com.baiyi.opscloud.facade.WorkorderFacade;
import com.baiyi.opscloud.service.workorder.OcWorkorderGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:13 下午
 * @Version 1.0
 */
@Service
public class WorkorderFacadeImpl implements WorkorderFacade {

    @Resource
    private OcWorkorderGroupService ocWorkorderGroupService;

    @Resource
    private WorkorderGroupDecorator workorderGroupDecorator;

    @Override
    public DataTable<OcWorkorderGroupVO.WorkorderGroup> queryWorkorderGroupPage(WorkorderGroupParam.PageQuery pageQuery) {
        DataTable<OcWorkorderGroup> table = ocWorkorderGroupService.queryOcWorkorderGroupByParam(pageQuery);
        List<OcWorkorderGroupVO.WorkorderGroup> page = BeanCopierUtils.copyListProperties(table.getData(), OcWorkorderGroupVO.WorkorderGroup.class);
        DataTable<OcWorkorderGroupVO.WorkorderGroup> dataTable = new DataTable<>(page, table.getTotalNum());
        return dataTable;
    }

    @Override
    public List<OcWorkorderGroupVO.WorkorderGroup> queryWorkbenchWorkorderGroup() {
        List<OcWorkorderGroupVO.WorkorderGroup> list = BeanCopierUtils.copyListProperties(ocWorkorderGroupService.queryOcWorkorderGroupAll(), OcWorkorderGroupVO.WorkorderGroup.class);
        return list.stream().map(e -> workorderGroupDecorator.decorator(e)).collect(Collectors.toList());
    }

}
