package com.baiyi.opscloud.decorator.aliyun.slb;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbHttpsListenerService;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbSCService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:41 下午
 * @Since 1.0
 */
@Component
public class AliyunSLBSCDecorator {

    @Resource
    private OcAliyunSlbHttpsListenerService ocAliyunSlbHttpsListenerService;

    @Resource
    private OcAliyunSlbSCService ocAliyunSlbSCService;

    public AliyunSLBVO.ServerCertificate decoratorVO(OcAliyunSlbSc ocAliyunSlbSc) {
        AliyunSLBVO.ServerCertificate serverCertificate =
                BeanCopierUtils.copyProperties(ocAliyunSlbSc, AliyunSLBVO.ServerCertificate.class);
        Integer expirationCurrDateDiff = (int) ((ocAliyunSlbSc.getExpireTimeStamp() - new Date().getTime()) / (24 * 60 * 60 * 1000));
        serverCertificate.setExpirationCurrDateDiff(expirationCurrDateDiff);
        List<OcAliyunSlbHttpsListener> httpsListenerList =
                ocAliyunSlbHttpsListenerService.queryOcAliyunSlbHttpsListenerBySCId(ocAliyunSlbSc.getServerCertificateId());
        serverCertificate.setHttpsListenerList(httpsListenerList);
        if (ocAliyunSlbSc.getUpdateServerCertificateId() != null) {
            OcAliyunSlbSc newSCName = ocAliyunSlbSCService.queryOcAliyunSlbScBySCId(ocAliyunSlbSc.getUpdateServerCertificateId());
            String newName = newSCName != null ? newSCName.getServerCertificateName() : "unknown";
            serverCertificate.setUpdateServerCertificateName(newName);
        }
        return serverCertificate;
    }

    public List<AliyunSLBVO.ServerCertificate> decoratorVOList(List<OcAliyunSlbSc> ocAliyunSlbScList) {
        List<AliyunSLBVO.ServerCertificate> serverCertificateList = Lists.newArrayListWithCapacity(ocAliyunSlbScList.size());
        ocAliyunSlbScList.forEach(ocAliyunSlbSc -> serverCertificateList.add(decoratorVO(ocAliyunSlbSc)));
        return serverCertificateList;
    }
}
