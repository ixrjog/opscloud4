package com.baiyi.opscloud.service.fault.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultRootCauseType;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import com.baiyi.opscloud.mapper.opscloud.OcFaultRootCauseTypeMapper;
import com.baiyi.opscloud.service.fault.OcFaultRootCauseTypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 4:19 下午
 * @Since 1.0
 */

@Service
public class OcFaultRootCauseTypeServiceImpl implements OcFaultRootCauseTypeService {

    @Resource
    private OcFaultRootCauseTypeMapper ocFaultRootCauseTypeMapper;

    @Override
    public void addOcFaultRootCauseType(OcFaultRootCauseType ocFaultRootCauseType) {
        ocFaultRootCauseTypeMapper.insert(ocFaultRootCauseType);
    }

    @Override
    public DataTable<OcFaultRootCauseType> queryOcFaultRootCauseTypePage(FaultParam.RootCauseTypePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(OcFaultRootCauseType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("rootCauseType", pageQuery.getQueryName());
        List<OcFaultRootCauseType> ocFaultRootCauseTypeList = ocFaultRootCauseTypeMapper.selectByExample(example);
        return new DataTable<>(ocFaultRootCauseTypeList, page.getTotal());
    }

    @Override
    public OcFaultRootCauseType queryOcFaultRootCauseType(int id) {
        return ocFaultRootCauseTypeMapper.selectByPrimaryKey(id);
    }
}
