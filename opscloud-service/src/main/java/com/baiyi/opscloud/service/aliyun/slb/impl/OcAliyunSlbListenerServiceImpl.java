package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbListener;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbListenerMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbListenerService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:52 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSlbListenerServiceImpl implements OcAliyunSlbListenerService {

    @Resource
    private OcAliyunSlbListenerMapper ocAliyunSlbListenerMapper;

    @Override
    public OcAliyunSlbListener queryOcAliyunSlbListenerById(Integer id) {
        return ocAliyunSlbListenerMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunSlbListenerList(List<OcAliyunSlbListener> ocAliyunSlbListenerList) {
        ocAliyunSlbListenerMapper.insertList(ocAliyunSlbListenerList);
    }

    @Override
    public void updateOcAliyunSlbListener(OcAliyunSlbListener ocAliyunSlbListener) {
        ocAliyunSlbListenerMapper.updateByPrimaryKey(ocAliyunSlbListener);
    }

    @Override
    public void deleteOcAliyunSlbListenerByLoadBalancerId(String loadBalancerId) {
        Example example = new Example(OcAliyunSlbListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", loadBalancerId);
        ocAliyunSlbListenerMapper.deleteByExample(example);
    }

    @Override
    public List<OcAliyunSlbListener> queryOcAliyunSlbListenerByLoadBalancerId(String loadBalancerId) {
        Example example = new Example(OcAliyunSlbListener.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", loadBalancerId);
        return ocAliyunSlbListenerMapper.selectByExample(example);
    }
}
