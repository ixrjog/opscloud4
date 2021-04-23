package com.baiyi.opscloud.cloud.slb.impl;

import com.aliyuncs.slb.model.v20140515.DescribeServerCertificatesResponse;
import com.baiyi.opscloud.aliyun.slb.AliyunSLBSC;
import com.baiyi.opscloud.cloud.slb.AliyunSLBSCCenter;
import com.baiyi.opscloud.cloud.slb.builder.OcAliyunSlbSCBuilder;
import com.baiyi.opscloud.common.base.AliyunSLBSCType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbHttpsListenerService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbSCService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 4:13 下午
 * @Since 1.0
 */

@Component("AliyunSLBSCCenter")
public class AliyunSLBSCCenterImpl implements AliyunSLBSCCenter {

    @Resource
    private AliyunSLBSC aliyunSLBSC;

    @Resource
    private OcAliyunSlbSCService ocAliyunSlbSCService;

    @Resource
    private OcAliyunSlbHttpsListenerService ocAliyunSlbHttpsListenerService;

    @Override
    public BusinessWrapper<Boolean> syncSLBSC() {
        HashMap<String, OcAliyunSlbSc> map = getSLBServerCertificateMap();
        List<DescribeServerCertificatesResponse.ServerCertificate> serverCertificateList = aliyunSLBSC.queryServerCertificateList();
        serverCertificateList.forEach(serverCertificate -> {
            saveSLBServerCertificate(serverCertificate);
            map.remove(serverCertificate.getServerCertificateId());
        });
        delSLBServerCertificateByMap(map);
        return BusinessWrapper.SUCCESS;
    }

    private void saveSLBServerCertificate(DescribeServerCertificatesResponse.ServerCertificate serverCertificate) {
        OcAliyunSlbSc ocAliyunSlbSc =
                ocAliyunSlbSCService.queryOcAliyunSlbScBySCId(serverCertificate.getServerCertificateId());
        OcAliyunSlbSc newOcAliyunSlbSc = OcAliyunSlbSCBuilder.build(serverCertificate);
        if (ocAliyunSlbSc == null) {
            try {
                ocAliyunSlbSCService.addOcAliyunSlbSc(newOcAliyunSlbSc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newOcAliyunSlbSc.setId(ocAliyunSlbSc.getId());
            if (ocAliyunSlbSc.getUpdateServerCertificateId() != null)
                newOcAliyunSlbSc.setUpdateServerCertificateId(ocAliyunSlbSc.getUpdateServerCertificateId());
            ocAliyunSlbSCService.updateOcAliyunSlbSc(newOcAliyunSlbSc);
        }
    }

    private void delSLBServerCertificateByMap(HashMap<String, OcAliyunSlbSc> map) {
        map.forEach((key, value) -> ocAliyunSlbSCService.deleteOcAliyunSlbSc(value.getId()));
    }

    private HashMap<String, OcAliyunSlbSc> getSLBServerCertificateMap() {
        List<OcAliyunSlbSc> slbServerCertificateList = ocAliyunSlbSCService.queryOcAliyunSlbScAll();
        HashMap<String, OcAliyunSlbSc> map = Maps.newHashMap();
        slbServerCertificateList.forEach(serverCertificate -> map.put(serverCertificate.getServerCertificateId(), serverCertificate));
        return map;
    }


    @Override
    public BusinessWrapper<Boolean> setUpdateSC(AliyunSLBParam.SetUpdateSC param) {
        OcAliyunSlbSc ocAliyunSlbSc = ocAliyunSlbSCService.queryOcAliyunSlbScById(param.getId());
        ocAliyunSlbSc.setUpdateServerCertificateId(param.getUpdateServerCertificateId());
        ocAliyunSlbSCService.updateOcAliyunSlbSc(ocAliyunSlbSc);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> replaceSC(AliyunSLBParam.ReplaceSC param) {
        if (param.getServerCertificateType().equals(AliyunSLBSCType.DEFAULT.getCode())) {
            return replaceDefaultServerCertificate(param);
        }
        if (param.getServerCertificateType().equals(AliyunSLBSCType.EXTENSION.getCode())) {
            return replaceExtensionServerCertificate(param);
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_SLB_SC_TYPE_ERR);
    }

    private BusinessWrapper<Boolean> replaceDefaultServerCertificate(AliyunSLBParam.ReplaceSC param) {
        if (param.getListenerPort() == null || param.getLoadBalancerId() == null)
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_SLB_SC_UPDATE_PARAM_ERR);
        Boolean result = aliyunSLBSC.replaceDefaultServerCertificate(param);
        if (result) {
            AliyunSLBParam.HttpsListenerQuery queryParam = new AliyunSLBParam.HttpsListenerQuery();
            queryParam.setHttpsListenerPort(param.getListenerPort());
            queryParam.setLoadBalancerId(param.getLoadBalancerId());
            OcAliyunSlbHttpsListener listener = ocAliyunSlbHttpsListenerService.queryDefaultOcAliyunSlbHttpsListenerByHttpsListenerParam(queryParam);
            listener.setServerCertificateId(param.getUpdateServerCertificateId());
            ocAliyunSlbHttpsListenerService.updateOcAliyunSlbHttpsListener(listener);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_SLB_SC_UPDATE_FAIL);
    }

    private BusinessWrapper<Boolean> replaceExtensionServerCertificate(AliyunSLBParam.ReplaceSC param) {
        if (param.getDomainExtensionId() == null)
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_SLB_SC_UPDATE_PARAM_ERR);
        Boolean result = aliyunSLBSC.replaceExtensionServerCertificate(param);
        if (result) {
            OcAliyunSlbHttpsListener listener =
                    ocAliyunSlbHttpsListenerService.queryExtensionOcAliyunSlbHttpsListenerByDomainExtensionId(param.getDomainExtensionId());
            listener.setServerCertificateId(param.getUpdateServerCertificateId());
            ocAliyunSlbHttpsListenerService.updateOcAliyunSlbHttpsListener(listener);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_SLB_SC_UPDATE_FAIL);
    }
}
