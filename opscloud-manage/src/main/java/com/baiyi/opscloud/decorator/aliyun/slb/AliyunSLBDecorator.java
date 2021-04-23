package com.baiyi.opscloud.decorator.aliyun.slb;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbListenerService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:13 下午
 * @Since 1.0
 */

@Component
public class AliyunSLBDecorator {

    @Resource
    private OcAliyunSlbListenerService ocAliyunSlbListenerService;

    @Resource
    private AliyunSLBListenerDecorator aliyunSLBListenerDecorator;

    private AliyunSLBVO.LoadBalancer decoratorVO(OcAliyunSlb ocAliyunSlb) {
        AliyunSLBVO.LoadBalancer loadBalancer = BeanCopierUtils.copyProperties(ocAliyunSlb, AliyunSLBVO.LoadBalancer.class);
        List<AliyunSLBVO.Listener> listenerList =
                aliyunSLBListenerDecorator.decoratorVOList(ocAliyunSlbListenerService.queryOcAliyunSlbListenerByLoadBalancerId(ocAliyunSlb.getLoadBalancerId()));
        loadBalancer.setListenerList(listenerList);
        return loadBalancer;
    }

    public List<AliyunSLBVO.LoadBalancer> decoratorVOList(List<OcAliyunSlb> ocAliyunSlbList) {
        List<AliyunSLBVO.LoadBalancer> loadBalancerList = Lists.newArrayListWithCapacity(ocAliyunSlbList.size());
        ocAliyunSlbList.forEach(slb -> loadBalancerList.add(decoratorVO(slb)));
        return loadBalancerList;
    }
}
