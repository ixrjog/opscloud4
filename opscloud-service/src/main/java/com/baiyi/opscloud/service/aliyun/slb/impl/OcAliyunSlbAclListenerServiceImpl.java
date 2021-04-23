package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbAclListenerMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclListenerService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:05 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSlbAclListenerServiceImpl implements OcAliyunSlbAclListenerService {

    @Resource
    private OcAliyunSlbAclListenerMapper ocAliyunSlbAclListenerMapper;

    @Override
    public void addOcAliyunSlbAclListenerList(List<OcAliyunSlbAclListener> ocAliyunSlbAclListenerList) {
        ocAliyunSlbAclListenerMapper.insertList(ocAliyunSlbAclListenerList);
    }

    @Override
    public void deleteOcAliyunSlbAclListenerBySlbAclId(String slbAclId) {
        Example example = new Example(OcAliyunSlbAclListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("slbAclId", slbAclId);
        ocAliyunSlbAclListenerMapper.deleteByExample(example);
    }

    @Override
    public OcAliyunSlbAclListener queryOcAliyunSlbAclListenerByLoadBalancerIdAndListenerPort(String loadBalancerId, Integer slbAclListenerPort) {
        Example example = new Example(OcAliyunSlbAclListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", loadBalancerId);
        criteria.andEqualTo("slbAclListenerPort", slbAclListenerPort);
        return ocAliyunSlbAclListenerMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAliyunSlbAclListener> queryOcAliyunSlbAclListenerBySlbAclId(String slbAclId) {
        Example example = new Example(OcAliyunSlbAclListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("slbAclId", slbAclId);
        return ocAliyunSlbAclListenerMapper.selectByExample(example);
    }
}
