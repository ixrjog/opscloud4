package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:14 下午
 * @Version 1.0
 */
public interface OcWorkorderGroupService {

    DataTable<OcWorkorderGroup> queryOcWorkorderGroupByParam(WorkorderGroupParam.PageQuery pageQuery);

    List<OcWorkorderGroup> queryOcWorkorderGroupAll();

    void addOcWorkorderGroup(OcWorkorderGroup ocWorkorderGroup);

    void updateOcWorkorderGroup(OcWorkorderGroup ocWorkorderGroup);

    void deleteOcWorkorderGroupById(int id);
}
