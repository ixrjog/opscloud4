package com.baiyi.caesar.sshserver.config;

import com.baiyi.caesar.common.type.UserCredentialTypeEnum;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserCredential;
import com.baiyi.caesar.service.user.UserCredentialService;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.sshserver.auth.PublickeyAuthenticatorProvider;
import com.baiyi.caesar.sshserver.auth.SshShellAuthenticationProvider;
import com.baiyi.caesar.sshserver.auth.SshShellPublicKeyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/10 2:16 下午
 * @Since 1.0
 */
@Configuration
public class SshAuthenticationConfig {

    @Resource
    private UserService userService;

    @Resource
    private UserCredentialService userCredentialService;

    @Bean
    @Primary
    public SshShellAuthenticationProvider passwordAuthenticatorProvider() {
        return (username, pass, serverSession) -> {
            User user = userService.getByUsername(username);
            return pass.equals(user.getPassword());
        };
    }

    @Bean
    @Primary
    public PublickeyAuthenticatorProvider publickeyAuthenticatorProvider() {
        return (username, publicKey, serverSession) -> {
            try {
                User user = userService.getByUsername(username);
                List<UserCredential> userCredentialList = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.PUB_KEY.getType());
                if (CollectionUtils.isEmpty(userCredentialList))
                    return false;
                File tmp = Files.createTempFile("sshShellPubKeys-", ".tmp").toFile();
                FileWriter fw = new FileWriter(tmp);
                for (UserCredential userCredential : userCredentialList)
                    fw.write(userCredential.getCredential() + "\n");
                fw.close();
                boolean result = new SshShellPublicKeyAuthenticationProvider(tmp).authenticate(username, publicKey, serverSession);
                tmp.delete();
                return result;
            } catch (Exception ignored) {
            }
            return false;
        };
    }

}
