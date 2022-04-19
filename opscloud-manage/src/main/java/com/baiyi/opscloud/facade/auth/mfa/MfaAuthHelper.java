package com.baiyi.opscloud.facade.auth.mfa;

import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.otp.OtpUtil;
import com.baiyi.opscloud.otp.exception.OtpException;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/28 4:51 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MfaAuthHelper {

    private final UserCredentialService userCredentialService;

    private final UserService userService;

    /**
     * MFA 验证
     *
     * @param user
     * @param loginParam
     */
    public void verify(User user, LoginParam.Login loginParam) {
        if (StringUtils.isEmpty(loginParam.getOtp()))
            throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_OTP_FAILURE); // OTP
        List<UserCredential> credentials = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.OTP_SK.getType());
        try {
            SecretKey key = OtpUtil.toKey(credentials.get(0).getCredential());
            final String otp = OtpUtil.generateOtp(key);
            if (!otp.equals(loginParam.getOtp()))
                throw new AuthRuntimeException(ErrorEnum.AUTH_USER_LOGIN_OTP_FAILURE); // 登录失败
        } catch (OtpException.DecodingException e) {
            throw new AuthRuntimeException("MFA认证失败: Decoding Exception!");
        } catch (NoSuchAlgorithmException e2) {
            throw new AuthRuntimeException("MFA认证失败: No Such Algorithm Exception!");
        } catch (InvalidKeyException e3) {
            throw new AuthRuntimeException("MFA认证失败: Invalid Key Exception!");
        }
    }

    /**
     * MFA 尝试绑定
     *
     * @param user
     * @param loginParam
     */
    public boolean tryBind(User user, LoginParam.Login loginParam) {
        // 用户未提交OTP
        if (StringUtils.isBlank(loginParam.getOtp())) return false;
        try {
            verify(user, loginParam);
        } catch (Exception e) {
            log.info("尝试绑定MFA失败用户提交的OTP不正确: username = {}", loginParam.getUsername());
            return false;
        }
        return true;
    }
}
