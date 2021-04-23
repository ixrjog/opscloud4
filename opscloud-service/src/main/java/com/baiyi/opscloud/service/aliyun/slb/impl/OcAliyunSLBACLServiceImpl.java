package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbAclMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBACLService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:01 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSLBACLServiceImpl implements OcAliyunSLBACLService {

    @Resource
    private OcAliyunSlbAclMapper ocAliyunSlbAclMapper;

    @Override
    public OcAliyunSlbAcl queryOcAliyunSlbAcl(Integer id) {
        return ocAliyunSlbAclMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunSlbAcl(OcAliyunSlbAcl ocAliyunSlbAcl) {
        ocAliyunSlbAclMapper.insert(ocAliyunSlbAcl);
    }

    @Override
    public void updateOcAliyunSlbAcl(OcAliyunSlbAcl ocAliyunSlbAcl) {
        ocAliyunSlbAclMapper.updateByPrimaryKey(ocAliyunSlbAcl);
    }

    @Override
    public void deleteOcAliyunSlbAcl(int id) {
        ocAliyunSlbAclMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAliyunSlbAcl queryOcAliyunSlbAclBySlbAclId(String slbAclId) {
        Example example = new Example(OcAliyunSlbAcl.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("slbAclId", slbAclId);
        return ocAliyunSlbAclMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAliyunSlbAcl> queryOcAliyunSlbAclAll() {
        return ocAliyunSlbAclMapper.selectAll();
    }

    @Override
    public DataTable<OcAliyunSlbAcl> queryOcAliyunSlbAclPage(AliyunSLBParam.ACLPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunSlbAcl> slbACLList = ocAliyunSlbAclMapper.queryOcAliyunSlbAclPage(pageQuery);
        return new DataTable<>(slbACLList, page.getTotal());
    }
}
