package com.baiyi.opscloud.service.fault.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultAction;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import com.baiyi.opscloud.mapper.opscloud.OcFaultActionMapper;
import com.baiyi.opscloud.service.fault.OcFaultActionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 3:52 下午
 * @Since 1.0
 */

@Service
public class OcFaultActionServiceImpl implements OcFaultActionService {

    @Resource
    private OcFaultActionMapper ocFaultActionMapper;

    @Override
    public void addOcFaultActionList(List<OcFaultAction> ocFaultActionList) {
        ocFaultActionMapper.insertList(ocFaultActionList);
    }

    @Override
    public void addOcFaultAction(OcFaultAction ocFaultAction) {
        ocFaultActionMapper.insert(ocFaultAction);
    }

    @Override
    public void updateOcFaultAction(OcFaultAction ocFaultAction) {
        ocFaultActionMapper.updateByPrimaryKey(ocFaultAction);
    }

    @Override
    public List<OcFaultAction> queryOcFaultActionByFaultId(Integer faultId) {
        Example example = new Example(OcFaultAction.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("faultId", faultId);
        return ocFaultActionMapper.selectByExample(example);
    }

    @Override
    public void delOcFaultActionByFaultId(Integer faultId) {
        Example example = new Example(OcFaultAction.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("faultId", faultId);
        ocFaultActionMapper.deleteByExample(example);
    }

    @Override
    public DataTable<OcFaultAction> queryFaultActionPage(FaultParam.ActionPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcFaultAction> ocFaultActionList = ocFaultActionMapper.queryFaultActionPage(pageQuery);
        return new DataTable<>(ocFaultActionList, page.getTotal());
    }
}
