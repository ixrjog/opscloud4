package com.baiyi.opscloud.datasource.aliyun.ram.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysRequest;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.AccessKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/10 2:52 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunRamAccessKeyDriver {

    private final AliyunClient aliyunClient;

    public List<AccessKey.Key> listAccessKeys(String regionId, AliyunConfig.Aliyun aliyun, String username) {
        try {
            ListAccessKeysRequest request = new ListAccessKeysRequest();
            request.setUserName(username);
            ListAccessKeysResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return BeanCopierUtil.copyListProperties(response.getAccessKeys(),AccessKey.Key.class);
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

}