package com.baiyi.opscloud.service.fault.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcFaultResponsible;
import com.baiyi.opscloud.mapper.opscloud.OcFaultResponsibleMapper;
import com.baiyi.opscloud.service.fault.OcFaultResponsibleService;
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
public class OcFaultResponsibleServiceImpl implements OcFaultResponsibleService {

    @Resource
    private OcFaultResponsibleMapper ocFaultResponsibleMapper;

    @Override
    public void addOcFaultResponsibleList(List<OcFaultResponsible> ocFaultResponsibleList) {
        ocFaultResponsibleMapper.insertList(ocFaultResponsibleList);
    }

    @Override
    public void delOcFaultResponsibleByFaultId(Integer faultId) {
        Example example = new Example(OcFaultResponsible.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("faultId", faultId);
        ocFaultResponsibleMapper.deleteByExample(example);
    }

    @Override
    public void updateOcFaultResponsible(OcFaultResponsible ocFaultResponsible) {
        ocFaultResponsibleMapper.updateByPrimaryKey(ocFaultResponsible);
    }

    @Override
    public List<OcFaultResponsible> queryOcFaultResponsibleByFaultId(Integer faultId) {
        Example example = new Example(OcFaultResponsible.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("faultId", faultId);
        return ocFaultResponsibleMapper.selectByExample(example);
    }
}
