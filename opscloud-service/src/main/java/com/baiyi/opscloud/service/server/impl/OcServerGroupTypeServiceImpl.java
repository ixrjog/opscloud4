package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupType;
import com.baiyi.opscloud.domain.param.server.ServerGroupTypeParam;
import com.baiyi.opscloud.mapper.opscloud.OcServerGroupTypeMapper;
import com.baiyi.opscloud.service.server.OcServerGroupTypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 1:12 下午
 * @Version 1.0
 */
@Service
public class OcServerGroupTypeServiceImpl implements OcServerGroupTypeService {

    @Resource
    private OcServerGroupTypeMapper ocServerGroupTypeMapper;

    @Override
    public OcServerGroupType queryOcServerGroupTypeById(Integer id) {
        return ocServerGroupTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public  OcServerGroupType queryOcServerGroupTypeByGrpType(Integer grpType){
        Example example = new Example(OcServerGroupType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("grpType", grpType);
        return ocServerGroupTypeMapper.selectOneByExample(example);
    }

    @Override
    public OcServerGroupType queryOcServerGroupTypeByName(String name) {
        Example example = new Example(OcServerGroupType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return ocServerGroupTypeMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcServerGroupType> queryOcServerGroupTypeByParam(ServerGroupTypeParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcServerGroupType> ocServerGroupTypeList = ocServerGroupTypeMapper.queryOcServerGroupTypeByParam(pageQuery);
        return new DataTable<>(ocServerGroupTypeList, page.getTotal());
    }

    @Override
    public void addOcServerGroupType(OcServerGroupType ocServerGroupType) {
        ocServerGroupTypeMapper.insert(ocServerGroupType);
    }

    @Override
    public void updateOcServerGroupType(OcServerGroupType ocServerGroupType) {
        ocServerGroupTypeMapper.updateByPrimaryKey(ocServerGroupType);
    }

    @Override
    public void deleteOcServerGroupTypeById(int id) {
        ocServerGroupTypeMapper.deleteByPrimaryKey(id);
    }
}
