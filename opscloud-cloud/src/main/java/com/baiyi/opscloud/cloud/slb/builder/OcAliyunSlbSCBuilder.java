package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeServerCertificatesResponse;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:33 下午
 * @Since 1.0
 */
public class OcAliyunSlbSCBuilder {

    public static OcAliyunSlbSc build(DescribeServerCertificatesResponse.ServerCertificate serverCertificate) {
        OcAliyunSlbSc ocAliyunSlbSc = new OcAliyunSlbSc();
        ocAliyunSlbSc.setServerCertificateId(serverCertificate.getServerCertificateId());
        ocAliyunSlbSc.setServerCertificateName(serverCertificate.getServerCertificateName());
        ocAliyunSlbSc.setRegionId(serverCertificate.getRegionId());
        ocAliyunSlbSc.setFingerprint(serverCertificate.getFingerprint());
        ocAliyunSlbSc.setAliCloudCertificateId(serverCertificate.getAliCloudCertificateId());
        ocAliyunSlbSc.setAliCloudCertificateName(serverCertificate.getAliCloudCertificateName());
        ocAliyunSlbSc.setCommonName(serverCertificate.getCommonName());
        ocAliyunSlbSc.setExpireTime(TimeUtils.acqGmtTimePlus(serverCertificate.getExpireTime()));
        ocAliyunSlbSc.setExpireTimeStamp(serverCertificate.getExpireTimeStamp());
        return ocAliyunSlbSc;
    }
}
