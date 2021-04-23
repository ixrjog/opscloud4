package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAuthGroup;
import com.baiyi.opscloud.domain.param.auth.GroupParam;
import com.baiyi.opscloud.mapper.opscloud.OcAuthGroupMapper;
import com.baiyi.opscloud.service.auth.OcAuthGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/11 2:08 下午
 * @Version 1.0
 */
@Service
public class OcAuthGroupServiceImpl implements OcAuthGroupService {

    @Resource
    private OcAuthGroupMapper ocAuthGroupMapper;

    @Override
    public OcAuthGroup queryOcAuthGroupById(int id) {
        return ocAuthGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<OcAuthGroup> queryOcAuthGroupByParam(GroupParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAuthGroup> ocAuthGroupList = ocAuthGroupMapper.queryOcAuthGroupByParam(pageQuery);
        return new DataTable<>(ocAuthGroupList, page.getTotal());
    }

    @Override
    public void addOcAuthGroup(OcAuthGroup ocAuthGroup) {
        ocAuthGroupMapper.insert(ocAuthGroup);
    }

    @Override
    public void updateOcAuthGroup(OcAuthGroup ocAuthGroup) {
        ocAuthGroupMapper.updateByPrimaryKey(ocAuthGroup);
    }

    @Override
    public void deleteOcAuthGroupById(int id) {
        ocAuthGroupMapper.deleteByPrimaryKey(id);
    }

}
