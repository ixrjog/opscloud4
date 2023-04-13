package com.baiyi.opscloud.datasource.aws.iam.driver;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.iam.service.AmazonIdentityManagementService;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/3/9 1:32 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class AmazonIdentityManagementMFADriver {

    /**
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_CreateVirtualMFADevice.html
     * 创建虚拟MFA设备
     *
     * @param config
     * @param user
     * @return
     */
    @Retryable(retryFor = RetryException.class, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public VirtualMFADevice createVirtualMFADevice(AwsConfig.Aws config, User user) throws RetryException {
        try {
            CreateVirtualMFADeviceRequest request = new CreateVirtualMFADeviceRequest();
            request.setVirtualMFADeviceName(user.getUsername());
            CreateVirtualMFADeviceResult result = buildAmazonIdentityManagement(config).createVirtualMFADevice(request);
            return result.getVirtualMFADevice();
        } catch (Exception e) {
            log.error("创建虚拟MFA设备错误: {}", e.getMessage());
            throw new RetryException("创建虚拟MFA设备错误: " + e.getMessage());
        }
    }

    /**
     * 启用MFA设备(与用户绑定)
     *
     * @param config
     * @param user
     * @param serialNumber arn:aws:iam::123456789012:mfa/ExampleName
     */
    public EnableMFADeviceResult enableMFADevice(AwsConfig.Aws config, User user, String serialNumber, String authenticationCode1, String authenticationCode2) {
        EnableMFADeviceRequest request = new EnableMFADeviceRequest();
        request.setUserName(user.getUsername());
        request.setSerialNumber(serialNumber);
        request.setAuthenticationCode1(authenticationCode1);
        request.setAuthenticationCode2(authenticationCode2);
        return buildAmazonIdentityManagement(config).enableMFADevice(request);
    }

    /**
     * 删除MFA设备
     *
     * @param config
     * @param serialNumber arn:aws:iam::123456789012:mfa/ExampleName
     */
    public void deleteVirtualMFADevice(AwsConfig.Aws config, String serialNumber) {
        DeleteVirtualMFADeviceRequest request = new DeleteVirtualMFADeviceRequest();
        request.setSerialNumber(serialNumber);
        buildAmazonIdentityManagement(config).deleteVirtualMFADevice(request);
    }

    private AmazonIdentityManagement buildAmazonIdentityManagement(AwsConfig.Aws aws) {
        return AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
    }

}