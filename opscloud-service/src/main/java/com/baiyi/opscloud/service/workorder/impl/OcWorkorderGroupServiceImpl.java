package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderGroupMapper;
import com.baiyi.opscloud.service.workorder.OcWorkorderGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/26 1:14 下午
 * @Version 1.0
 */
@Service
public class OcWorkorderGroupServiceImpl implements OcWorkorderGroupService {

    @Resource
    private OcWorkorderGroupMapper ocWorkorderGroupMapper;

    @Override
    public DataTable<OcWorkorderGroup> queryOcWorkorderGroupByParam(WorkorderGroupParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcWorkorderGroup> list = ocWorkorderGroupMapper.queryOcWorkorderGroupByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcWorkorderGroup> queryOcWorkorderGroupAll() {
        return ocWorkorderGroupMapper.selectAll();

    }

    @Override
    public void addOcWorkorderGroup(OcWorkorderGroup ocWorkorderGroup) {
        ocWorkorderGroupMapper.insert(ocWorkorderGroup);
    }

    @Override
    public void updateOcWorkorderGroup(OcWorkorderGroup ocWorkorderGroup) {
        ocWorkorderGroupMapper.updateByPrimaryKey(ocWorkorderGroup);
    }

    @Override
    public void deleteOcWorkorderGroupById(int id) {
        ocWorkorderGroupMapper.deleteByPrimaryKey(id);
    }
}
