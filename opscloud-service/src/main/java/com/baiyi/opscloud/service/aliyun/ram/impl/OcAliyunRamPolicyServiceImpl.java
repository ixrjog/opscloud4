package com.baiyi.opscloud.service.aliyun.ram.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamPolicy;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMPolicyParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunRamPolicyMapper;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamPolicyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 10:50 上午
 * @Version 1.0
 */
@Service
public class OcAliyunRamPolicyServiceImpl implements OcAliyunRamPolicyService {

    @Resource
    private OcAliyunRamPolicyMapper ocAliyunRamPolicyMapper;

    @Override
    public DataTable<OcAliyunRamPolicy> queryOcAliyunRamPolicyByParam(AliyunRAMPolicyParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunRamPolicy> list = ocAliyunRamPolicyMapper.queryOcAliyunRamPolicyByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcAliyunRamPolicy> queryOcAliyunRamPolicyByAccountUid(String accountUid) {
        Example example = new Example(OcAliyunRamPolicy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid);
        return ocAliyunRamPolicyMapper.selectByExample(example);
    }

    @Override
    public OcAliyunRamPolicy queryOcAliyunRamPolicyByUniqueKey(String accountUid, String policyName) {
        Example example = new Example(OcAliyunRamPolicy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid);
        criteria.andEqualTo("policyName", policyName);
        return ocAliyunRamPolicyMapper.selectOneByExample(example);
    }

    @Override
    public OcAliyunRamPolicy queryOcAliyunRamPolicyById(int id) {
        return ocAliyunRamPolicyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunRamPolicy(OcAliyunRamPolicy ocAliyunRamPolicy) {
        ocAliyunRamPolicyMapper.insert(ocAliyunRamPolicy);
    }

    @Override
    public void updateOcAliyunRamPolicy(OcAliyunRamPolicy ocAliyunRamPolicy) {
        ocAliyunRamPolicyMapper.updateByPrimaryKey(ocAliyunRamPolicy);
    }

    @Override
    public void deleteOcAliyunRamPolicyById(int id) {
        ocAliyunRamPolicyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcAliyunRamPolicy> queryOcAliyunRamPolicyByUserPermission(String accountUid, Integer ramUserId) {
        return ocAliyunRamPolicyMapper.queryOcAliyunRamPolicyByUserPermission(accountUid, ramUserId);
    }

    @Override
    public List<OcAliyunRamPolicy> queryUserTicketOcRamPolicyByParam(AliyunRAMPolicyParam.UserTicketOcRamPolicyQuery queryParam) {
        PageHelper.startPage(queryParam.getPage(), queryParam.getLength().intValue());
        return ocAliyunRamPolicyMapper.queryUserTicketOcRamPolicyByParam(queryParam);
    }

}
