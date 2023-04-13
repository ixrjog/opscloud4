package com.baiyi.opscloud.datasource.aws.iam.driver;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AccessKeyMetadata;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysRequest;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysResult;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamAccessKey;
import com.baiyi.opscloud.datasource.aws.iam.service.AmazonIdentityManagementService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/24 9:41 AM
 * @Version 1.0
 */
@Component
public class AmazonIdentityManagementAccessKeyDriver {

    public List<IamAccessKey.AccessKey> listAccessKeys(AwsConfig.Aws config, String userName) {
        ListAccessKeysRequest request = new ListAccessKeysRequest();
        request.setUserName(userName);
        List<AccessKeyMetadata> keyMetadata = Lists.newArrayList();
        while (true) {
            ListAccessKeysResult result = buildAmazonIdentityManagement(config).listAccessKeys(request);
            keyMetadata.addAll(result.getAccessKeyMetadata());
            if (result.getIsTruncated()) {
                request.setMarker(result.getMarker());
            } else {
                break;
            }
        }
        return BeanCopierUtil.copyListProperties(keyMetadata, IamAccessKey.AccessKey.class);
    }

    private AmazonIdentityManagement buildAmazonIdentityManagement(AwsConfig.Aws aws) {
        return AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
    }

}
