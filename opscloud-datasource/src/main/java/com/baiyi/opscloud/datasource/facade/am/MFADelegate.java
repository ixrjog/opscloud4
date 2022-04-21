package com.baiyi.opscloud.datasource.facade.am;

import com.amazonaws.services.identitymanagement.model.EnableMFADeviceResult;
import com.amazonaws.services.identitymanagement.model.VirtualMFADevice;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.iam.driver.AmazonIdentityManagementMFADriver;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.otp.OtpUtil;
import com.baiyi.opscloud.otp.model.OTPAccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/3/9 4:02 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MFADelegate {

    private final AmazonIdentityManagementMFADriver amazonIMMFADriver;

    private final ThreadLocal<Boolean> firstExecution = new ThreadLocal<>();

    @Retryable(value = RetryException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void enableMFADevice(AwsConfig.Aws config, User user, VirtualMFADevice vMFADevice) throws RetryException {
        try {
            if (firstExecution.get() == null) {
                log.info("首次执行延迟3秒！");
                firstExecution.set(Boolean.FALSE);
                TimeUnit.SECONDS.sleep(3L);
            }
        } catch (InterruptedException e) {
        }
        try {
            log.info("尝试启用IAM虚拟MFA: username = {} , serialNumber = {}", user.getUsername(), vMFADevice.getSerialNumber());
            String secretKeyStr = new String(vMFADevice.getBase32StringSeed().array());
            SecretKey key = OtpUtil.toKey(secretKeyStr);
            OTPAccessCode.AccessCode accessCode = OtpUtil.generateOtpAccessCode(key);
            EnableMFADeviceResult result = amazonIMMFADriver.enableMFADevice(config, user, vMFADevice.getSerialNumber(), accessCode.getCurrentPassword(), accessCode.getFuturePassword());
            log.info("启用虚拟MFA设备成功: username = {} , requestId = {}", user.getUsername(), result.getSdkResponseMetadata().getRequestId());
        } catch (Exception e) {
            log.error("启用虚拟MFA设备失败: {}", e.getMessage());
            throw new RetryException(e.getMessage());
        }
    }

}
