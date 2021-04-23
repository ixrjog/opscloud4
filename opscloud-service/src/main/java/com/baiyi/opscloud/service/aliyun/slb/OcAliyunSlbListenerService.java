package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbListener;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:37 下午
 * @Since 1.0
 */
public interface OcAliyunSlbListenerService {

    OcAliyunSlbListener queryOcAliyunSlbListenerById(Integer id);

    void addOcAliyunSlbListenerList(List<OcAliyunSlbListener> ocAliyunSlbListenerList);

    void updateOcAliyunSlbListener(OcAliyunSlbListener ocAliyunSlbListener);

    void deleteOcAliyunSlbListenerByLoadBalancerId(String loadBalancerId);

    List<OcAliyunSlbListener> queryOcAliyunSlbListenerByLoadBalancerId(String loadBalancerId);
}
