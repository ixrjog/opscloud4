package com.baiyi.opscloud.service.opscloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcInstance;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudInstanceParam;
import com.baiyi.opscloud.mapper.opscloud.OcInstanceMapper;
import com.baiyi.opscloud.service.opscloud.OcInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 5:20 下午
 * @Since 1.0
 */

@Service
public class OcInstanceServiceImpl implements OcInstanceService {

    @Resource
    private OcInstanceMapper ocInstanceMapper;

    @Override
    public DataTable<OcInstance> queryOcInstanceByParam(OpscloudInstanceParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcInstance> list = ocInstanceMapper.queryOcInstanceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcInstance(OcInstance ocInstance) {
        ocInstanceMapper.insert(ocInstance);
    }

    @Override
    public void updateOcInstance(OcInstance ocInstance) {
        ocInstanceMapper.updateByPrimaryKey(ocInstance);
    }

    @Override
    public OcInstance queryOcInstanceByHostIp(String hostIp) {
        Example example = new Example(OcInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostIp", hostIp);
        return ocInstanceMapper.selectOneByExample(example);
    }

    @Override
    public OcInstance queryOcInstanceById(int id) {
        return ocInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delOcInstance(int id) {
        ocInstanceMapper.deleteByPrimaryKey(id);
    }
}
