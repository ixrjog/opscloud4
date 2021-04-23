package com.baiyi.opscloud.cloud.slb.impl;

import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerHTTPSListenerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.baiyi.opscloud.aliyun.slb.AliyunSLB;
import com.baiyi.opscloud.cloud.slb.AliyunSLBCenter;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSLBBuilder;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSLBHttpsListenerBuilder;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSLBListenerBuilder;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbHttpsListenerService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbListenerService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbSCService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:13 下午
 * @Since 1.0
 */

@Component("AliyunSLBCenter")
public class AliyunSLBCenterImpl implements AliyunSLBCenter {

    @Resource
    private AliyunSLB aliyunSLB;

    @Resource
    private OcAliyunSLBService ocAliyunSLBService;

    @Resource
    private OcAliyunSlbListenerService ocAliyunSlbListenerService;

    @Resource
    private OcAliyunSlbHttpsListenerService ocAliyunSlbHttpsListenerService;

    @Resource
    private OcAliyunSlbSCService ocAliyunSlbSCService;

    @Override
    public BusinessWrapper<Boolean> syncSLB() {
        HashMap<String, OcAliyunSlb> map = getSLBMap();
        List<DescribeLoadBalancersResponse.LoadBalancer> slbList = aliyunSLB.queryAliyunSLBList();
        slbList.forEach(slb -> {
            saveSLB(slb);
            map.remove(slb.getLoadBalancerId());
            refreshSLBListener(slb.getLoadBalancerId());
        });
        delSLBByMap(map);
        return BusinessWrapper.SUCCESS;
    }

    private void saveSLB(DescribeLoadBalancersResponse.LoadBalancer loadBalancer) {
        OcAliyunSlb ocAliyunSlb = ocAliyunSLBService.queryOcAliyunSlbByLoadBalancerId(loadBalancer.getLoadBalancerId());
        OcAliyunSlb newOcAliyunSlb = OcAliyunSLBBuilder.build(loadBalancer);
        if (ocAliyunSlb == null) {
            try {
                ocAliyunSLBService.addOcAliyunSlb(newOcAliyunSlb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newOcAliyunSlb.setId(ocAliyunSlb.getId());
            newOcAliyunSlb.setIsLinkNginx(ocAliyunSlb.getIsLinkNginx());
            ocAliyunSLBService.updateOcAliyunSlb(newOcAliyunSlb);
        }
    }

    private void delSLBByMap(HashMap<String, OcAliyunSlb> map) {
        map.forEach((key, value) -> {
            ocAliyunSLBService.deleteOcAliyunSlb(value.getId());
            ocAliyunSlbListenerService.deleteOcAliyunSlbListenerByLoadBalancerId(value.getLoadBalancerId());
            ocAliyunSlbHttpsListenerService.deleteOcAliyunSlbHttpsListenerByLoadBalancerId(value.getLoadBalancerId());
        });
    }

    private HashMap<String, OcAliyunSlb> getSLBMap() {
        List<OcAliyunSlb> slbList = ocAliyunSLBService.queryOcAliyunSlbAll();
        HashMap<String, OcAliyunSlb> map = Maps.newHashMap();
        slbList.forEach(slb -> map.put(slb.getLoadBalancerId(), slb));
        return map;
    }

    @Override
    public BusinessWrapper<Boolean> refreshSLBListener(String loadBalancerId) {
        ocAliyunSlbListenerService.deleteOcAliyunSlbListenerByLoadBalancerId(loadBalancerId);
        List<DescribeLoadBalancerAttributeResponse.ListenerPortAndProtocol> listenList = aliyunSLB.queryLoadBalancerListener(loadBalancerId);
        List<OcAliyunSlbListener> ocAliyunSlbListenerList = OcAliyunSLBListenerBuilder.buildList(loadBalancerId, listenList);
        if (CollectionUtils.isEmpty(ocAliyunSlbListenerList))
            return BusinessWrapper.SUCCESS;
        try {
            ocAliyunSlbListenerService.addOcAliyunSlbListenerList(ocAliyunSlbListenerList);
            refreshSLBHttpsListener(loadBalancerId);
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_SLB_SAVE_FAIL);
    }

    private void refreshSLBHttpsListener(String loadBalancerId) {
        List<OcAliyunSlbListener> listenerList = ocAliyunSlbListenerService.queryOcAliyunSlbListenerByLoadBalancerId(loadBalancerId);
        listenerList.forEach(listener -> {
            if (listener.getListenerProtocol().equals("https")) {
                AliyunSLBParam.HttpsListenerQuery param = new AliyunSLBParam.HttpsListenerQuery();
                param.setLoadBalancerId(loadBalancerId);
                param.setHttpsListenerPort(listener.getListenerPort());
                refreshSLBHttpsListenerSC(param);
            }
        });
    }

    private void refreshSLBHttpsListenerSC(AliyunSLBParam.HttpsListenerQuery param) {
        ocAliyunSlbHttpsListenerService.deleteOcAliyunSlbHttpsListenerByHttpsListenerParam(param);
        DescribeLoadBalancerHTTPSListenerAttributeResponse response = aliyunSLB.queryHttpsListenerDetail(param);
        if (response != null) {
            OcAliyunSlb slb = ocAliyunSLBService.queryOcAliyunSlbByLoadBalancerId(param.getLoadBalancerId());
            String loadBalancerName = slb != null ? slb.getLoadBalancerName() : "unknown";
            OcAliyunSlbSc ocAliyunSlbSc =
                    ocAliyunSlbSCService.queryOcAliyunSlbScBySCId(response.getServerCertificateId());
            String domain = ocAliyunSlbSc != null ? ocAliyunSlbSc.getCommonName() : "unknown";
            List<OcAliyunSlbHttpsListener> httpsListenerList = OcAliyunSLBHttpsListenerBuilder.build(param, response, domain, loadBalancerName);
            ocAliyunSlbHttpsListenerService.addOcAliyunSlbHttpsListenerList(httpsListenerList);
        }
    }


    @Override
    public List<DescribeHealthStatusResponse.BackendServer> querySLBListenerBackendServers(String loadBalancerId) {
        return aliyunSLB.queryLoadBalancerListenerBackendServers(loadBalancerId);
    }
}
