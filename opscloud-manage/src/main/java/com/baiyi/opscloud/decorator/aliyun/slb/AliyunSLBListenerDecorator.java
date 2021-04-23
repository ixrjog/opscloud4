package com.baiyi.opscloud.decorator.aliyun.slb;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbListener;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclListenerService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbHttpsListenerService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:14 下午
 * @Since 1.0
 */

@Component
public class AliyunSLBListenerDecorator {

    @Resource
    private OcAliyunSlbAclListenerService ocAliyunSlbAclListenerService;

    @Resource
    private OcAliyunSlbHttpsListenerService ocAliyunSlbHttpsListenerService;

    public AliyunSLBVO.Listener decoratorVO(OcAliyunSlbListener ocAliyunSlbListener) {
        AliyunSLBVO.Listener listener = BeanCopierUtils.copyProperties(ocAliyunSlbListener, AliyunSLBVO.Listener.class);
        OcAliyunSlbAclListener ocAliyunSlbAclListener = ocAliyunSlbAclListenerService.queryOcAliyunSlbAclListenerByLoadBalancerIdAndListenerPort(
                ocAliyunSlbListener.getLoadBalancerId(), ocAliyunSlbListener.getListenerPort());
        if (ocAliyunSlbAclListener != null)
            listener.setAccessControlListener(ocAliyunSlbAclListener);
        if (ocAliyunSlbListener.getListenerProtocol().equals("https")) {
            AliyunSLBParam.HttpsListenerQuery param = new AliyunSLBParam.HttpsListenerQuery();
            param.setHttpsListenerPort(ocAliyunSlbListener.getListenerPort());
            param.setLoadBalancerId(ocAliyunSlbListener.getLoadBalancerId());
            List<OcAliyunSlbHttpsListener> httpsListenerList =
                    ocAliyunSlbHttpsListenerService.queryOcAliyunSlbHttpsListenerByHttpsListenerParam(param);
            listener.setHttpsListenerList(httpsListenerList);
        }
        return listener;
    }

    public List<AliyunSLBVO.Listener> decoratorVOList(List<OcAliyunSlbListener> vcAliyunSlbListenerList) {
        List<AliyunSLBVO.Listener> listenerList = Lists.newArrayListWithCapacity(vcAliyunSlbListenerList.size());
        vcAliyunSlbListenerList.forEach(slb -> listenerList.add(decoratorVO(slb)));
        return listenerList;
    }
}
