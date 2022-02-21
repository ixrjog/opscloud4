package com.baiyi.opscloud.sshserver.config;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshserver.auth.PublickeyAuthenticatorProvider;
import com.baiyi.opscloud.sshserver.auth.SshShellAuthenticationProvider;
import com.baiyi.opscloud.sshserver.auth.SshShellPublicKeyAuthenticationProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2021/6/10 2:16 下午
 * @Since 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SshAuthenticationConfig {

    private final UserService userService;

    private final UserCredentialService userCredentialService;

    private final DsInstanceService dsInstanceService;

    private final DsInstanceAssetService dsInstanceAssetService;

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
            File tmp = null;
            FileWriter fw = null;
            try {
                User user = userService.getByUsername(username);
                Map<String, String> userSshKeyDict = getUserSshKeyDict(user);
                if (userSshKeyDict.isEmpty())
                    return false;
                tmp = Files.createTempFile("sshShellPubKeys-", ".tmp").toFile();
                fw = new FileWriter(tmp);
                for (String key : userSshKeyDict.keySet()) {
                    fw.write(userSshKeyDict.get(key) + "\n");
                }
                fw.close();
                return new SshShellPublicKeyAuthenticationProvider(tmp).authenticate(username, publicKey, serverSession);
            } catch (Exception e) {
                log.error("写入公钥错误: username = {}", username);
                log.error(e.getMessage());
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (Exception fwE) {
                    }
                }
                if (tmp != null)
                    tmp.delete();
            }
            return false;
        };
    }

    private Map<String, String> getUserSshKeyDict(User user) {
        Map<String, String> sshKeyDict = Maps.newHashMap();
        List<UserCredential> userCredentialList = userCredentialService.queryByUserIdAndType(user.getId(), UserCredentialTypeEnum.PUB_KEY.getType());
        userCredentialList.forEach(c ->
                sshKeyDict.put(c.getFingerprint(), c.getCredential())
        );
        List<DatasourceInstanceAsset> assets = querySshKeyAssets(user.getUsername());
        assets.forEach(a -> sshKeyDict.put(a.getAssetKey(), a.getAssetKey2()));
        return sshKeyDict;
    }

    public List<DatasourceInstanceAsset> querySshKeyAssets(String username) {
        DsInstanceParam.DsInstanceQuery instanceQuery = DsInstanceParam.DsInstanceQuery.builder()
                .instanceType(DsTypeEnum.GITLAB.getName())
                .build();

        List<DatasourceInstance> instances = dsInstanceService.queryByParam(instanceQuery);
        List<DatasourceInstanceAsset> result = Lists.newArrayList();
        instances.forEach(i -> {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .assetType(DsAssetTypeConstants.GITLAB_SSHKEY.name())
                    .instanceUuid(i.getUuid())
                    .name(username)
                    .build();
            result.addAll(dsInstanceAssetService.queryAssetByAssetParam(asset));
        });
        return result;
    }

}
