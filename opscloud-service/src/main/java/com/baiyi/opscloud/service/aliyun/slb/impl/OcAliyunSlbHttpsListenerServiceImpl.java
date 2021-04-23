package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbHttpsListenerMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbHttpsListenerService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:57 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSlbHttpsListenerServiceImpl implements OcAliyunSlbHttpsListenerService {

    @Resource
    private OcAliyunSlbHttpsListenerMapper ocAliyunSlbHttpsListenerMapper;

    @Override
    public void addOcAliyunSlbHttpsListenerList(List<OcAliyunSlbHttpsListener> ocAliyunSlbHttpsListenerList) {
        ocAliyunSlbHttpsListenerMapper.insertList(ocAliyunSlbHttpsListenerList);
    }

    @Override
    public void updateOcAliyunSlbHttpsListener(OcAliyunSlbHttpsListener ocAliyunSlbHttpsListener) {
        ocAliyunSlbHttpsListenerMapper.updateByPrimaryKey(ocAliyunSlbHttpsListener);
    }

    @Override
    public void deleteOcAliyunSlbHttpsListenerByHttpsListenerParam(AliyunSLBParam.HttpsListenerQuery param) {
        Example example = new Example(OcAliyunSlbHttpsListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", param.getLoadBalancerId());
        criteria.andEqualTo("httpsListenerPort", param.getHttpsListenerPort());
        ocAliyunSlbHttpsListenerMapper.deleteByExample(example);
    }

    @Override
    public void deleteOcAliyunSlbHttpsListenerByLoadBalancerId(String loadBalancerId) {
        Example example = new Example(OcAliyunSlbHttpsListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", loadBalancerId);
        ocAliyunSlbHttpsListenerMapper.deleteByExample(example);
    }

    @Override
    public List<OcAliyunSlbHttpsListener> queryOcAliyunSlbHttpsListenerByHttpsListenerParam(AliyunSLBParam.HttpsListenerQuery param) {
        Example example = new Example(OcAliyunSlbHttpsListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", param.getLoadBalancerId());
        criteria.andEqualTo("httpsListenerPort", param.getHttpsListenerPort());
        return ocAliyunSlbHttpsListenerMapper.selectByExample(example);
    }

    @Override
    public OcAliyunSlbHttpsListener queryDefaultOcAliyunSlbHttpsListenerByHttpsListenerParam(AliyunSLBParam.HttpsListenerQuery param) {
        Example example = new Example(OcAliyunSlbHttpsListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", param.getLoadBalancerId());
        criteria.andEqualTo("httpsListenerPort", param.getHttpsListenerPort());
        criteria.andEqualTo("serverCertificateType", 1);
        return ocAliyunSlbHttpsListenerMapper.selectOneByExample(example);
    }

    @Override
    public OcAliyunSlbHttpsListener queryExtensionOcAliyunSlbHttpsListenerByDomainExtensionId(String domainExtensionId) {
        Example example = new Example(OcAliyunSlbHttpsListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainExtensionId", domainExtensionId);
        criteria.andEqualTo("serverCertificateType", 2);
        return ocAliyunSlbHttpsListenerMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAliyunSlbHttpsListener> queryOcAliyunSlbHttpsListenerBySCId(String serverCertificateId) {
        Example example = new Example(OcAliyunSlbHttpsListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverCertificateId", serverCertificateId);
        return ocAliyunSlbHttpsListenerMapper.selectByExample(example);
    }
}
