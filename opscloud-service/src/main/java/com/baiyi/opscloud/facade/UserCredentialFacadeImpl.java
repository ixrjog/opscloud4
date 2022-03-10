package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.otp.Base32StringUtil;
import com.baiyi.opscloud.otp.OtpUtil;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/9 3:35 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserCredentialFacadeImpl implements UserCredentialFacade {

    private final UserCredentialService userCredentialService;

    private final UserService userService;

    @Override
    public void clearCredential(int userId, String instanceUuid, int credentialType) {
        List<UserCredential> credentials = userCredentialService.queryByUserIdAndType(userId, credentialType).stream()
                .filter(e -> instanceUuid.equals(e.getInstanceUuid()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(credentials)) {
            credentials.forEach(e -> userCredentialService.deleteById(e.getId()));
        }
    }

    @Override
    public void saveCredential(UserCredentialVO.Credential credential) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        if (user == null) return;
        saveCredential(credential, user);
    }

    @Override
    public void saveCredential(UserCredentialVO.Credential credential, User user) {
        credential.setUserId(user.getId());
        if (credential.getCredentialType() == UserCredentialTypeEnum.PUB_KEY.getType()) {
            savePubKey(credential);
            return;
        }
        UserCredential userCredential = BeanCopierUtil.copyProperties(credential, UserCredential.class);
        if (IdUtil.isEmpty(userCredential.getId())) {
            userCredentialService.add(userCredential);
        } else {
            userCredentialService.update(userCredential);
        }
    }

    @Override
    public void createMFACredential(User user) {
        // 判断是否重复申请
        int count = userCredentialService.countByUserIdAndType(user.getId(), UserCredentialTypeEnum.OTP_SK.getType());
        if (count > 0) return;
        String otpSK;
        try {
            Key key = OtpUtil.generateOtpSK();
            otpSK = Base32StringUtil.encode(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new CommonRuntimeException("生成OTP-SecretKey错误: " + e.getMessage());
        }
        UserCredential userCredential = UserCredential.builder()
                .userId(user.getId())
                .title(Joiner.on("@").join(user.getUsername(), "MFA"))
                .valid(true)
                .credentialType(UserCredentialTypeEnum.OTP_SK.getType())
                .credential(otpSK)
                .build();
        userCredentialService.add(userCredential);
    }

    private void savePubKey(UserCredentialVO.Credential credential) {
        List<String> result = Splitter.on(" ").splitToList(credential.getCredential());
        credential.setFingerprint(SSHUtil.getFingerprint(Joiner.on(" ").join(result.get(0), result.get(1)))); //计算指纹
        if (result.size() == 3)
            credential.setTitle(result.get(2));
        UserCredential userCredential = BeanCopierUtil.copyProperties(credential, UserCredential.class);
        if (IdUtil.isEmpty(userCredential.getId())) {
            userCredentialService.add(userCredential);
        } else {
            userCredentialService.update(userCredential);
        }
    }

}
