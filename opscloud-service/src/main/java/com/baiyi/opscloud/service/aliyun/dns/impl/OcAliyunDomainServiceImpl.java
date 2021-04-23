package com.baiyi.opscloud.service.aliyun.dns.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunDomainMapper;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 2:43 下午
 * @Since 1.0
 */

@Service
public class OcAliyunDomainServiceImpl implements OcAliyunDomainService {


    @Resource
    private OcAliyunDomainMapper ocAliyunDomainMapper;

    @Override
    public OcAliyunDomain queryAliyunDomainById(Integer id) {
        return ocAliyunDomainMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addAliyunDomain(OcAliyunDomain ocAliyunDomain) {
        ocAliyunDomainMapper.insert(ocAliyunDomain);
    }

    @Override
    public void updateAliyunDomain(OcAliyunDomain ocAliyunDomain) {
        ocAliyunDomainMapper.updateByPrimaryKey(ocAliyunDomain);
    }

    @Override
    public void deleteAliyunDomain(int id) {
        ocAliyunDomainMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAliyunDomain queryAliyunDomainByInstanceId(String instanceId) {
        Example example = new Example(OcAliyunDomain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocAliyunDomainMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAliyunDomain> queryAliyunDomainAll() {
        return ocAliyunDomainMapper.selectAll();
    }

    @Override
    public DataTable<OcAliyunDomain> queryAliyunDomainPage(AliyunDomainParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunDomain> ocAliyunDomainList = ocAliyunDomainMapper.queryOcAliyunDomainPage(pageQuery);
        return new DataTable<>(ocAliyunDomainList, page.getTotal());
    }
}
