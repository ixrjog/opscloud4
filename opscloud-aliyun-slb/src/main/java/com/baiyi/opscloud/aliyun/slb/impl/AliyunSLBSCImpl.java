package com.baiyi.opscloud.aliyun.slb.impl;

import com.aliyuncs.slb.model.v20140515.DescribeServerCertificatesResponse;
import com.baiyi.opscloud.aliyun.slb.AliyunSLBSC;
import com.baiyi.opscloud.aliyun.slb.handler.AliyunSLBSCHandler;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:32 上午
 * @Since 1.0
 */
@Component("AliyunSLBSC")
public class AliyunSLBSCImpl implements AliyunSLBSC {

    @Resource
    private AliyunSLBSCHandler aliyunSLBSCHandler;

    @Override
    public List<DescribeServerCertificatesResponse.ServerCertificate> queryServerCertificateList() {
        return aliyunSLBSCHandler.queryServerCertificateList();
    }

    @Override
    public Boolean replaceDefaultServerCertificate(AliyunSLBParam.ReplaceSC param) {
        return aliyunSLBSCHandler.replaceDefaultServerCertificate(param);
    }

    @Override
    public Boolean replaceExtensionServerCertificate(AliyunSLBParam.ReplaceSC param) {
        return aliyunSLBSCHandler.replaceExtensionServerCertificate(param);
    }
}
