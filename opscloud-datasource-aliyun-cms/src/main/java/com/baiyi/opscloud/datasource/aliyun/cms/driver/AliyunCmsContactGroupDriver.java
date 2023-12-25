package com.baiyi.opscloud.datasource.aliyun.cms.driver;

import com.aliyuncs.cms.model.v20190101.PutContactGroupRequest;
import com.aliyuncs.cms.model.v20190101.PutContactGroupResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/13 18:13
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunCmsContactGroupDriver {

    private final AliyunClient aliyunClient;

    /**
     * 创建报警联系组
     *
     * @param regionId
     * @param aliyun
     * @param application
     * @param contactNamess
     * @return
     */
    public boolean putContactGroup(String regionId, AliyunConfig.Aliyun aliyun, Application application, List<String> contactNamess) {
        PutContactGroupRequest request = new PutContactGroupRequest();
        request.setSysRegionId(regionId);
        request.setContactGroupName(application.getName());
        if (!CollectionUtils.isEmpty(contactNamess)) {
            request.setContactNamess(contactNamess);
        }
        if (StringUtils.isNotBlank(application.getComment())) {
            request.setDescribe(application.getComment());
        }
        try {
            PutContactGroupResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response.getSuccess();
        } catch (ClientException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}