package com.baiyi.opscloud.sshserver.config;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.common.type.UserCredentialTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshserver.auth.PublickeyAuthenticatorProvider;
import com.baiyi.opscloud.sshserver.auth.SshShellAuthenticationProvider;
import com.baiyi.opscloud.sshserver.auth.SshShellPublicKeyAuthenticationProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

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

    @Resource
    private DsInstanceService dsInstancService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

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
            try {
                User user = userService.getByUsername(username);
                Map<String,String> userSshKeyDict = getUserSshKeyDict(user);
                if(userSshKeyDict.isEmpty())
                    return false;
                tmp = Files.createTempFile("sshShellPubKeys-", ".tmp").toFile();
                FileWriter fw = new FileWriter(tmp);
                for(String key: userSshKeyDict.keySet())
                    fw.write(userSshKeyDict.get(key) + "\n");
                fw.close();
                boolean result = new SshShellPublicKeyAuthenticationProvider(tmp).authenticate(username, publicKey, serverSession);
                tmp.delete();
                return result;
            } catch (Exception ignored) {
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
        assets.forEach(a->{
            sshKeyDict.put(a.getAssetKey(),a.getAssetKey2());
        });
        return sshKeyDict;
    }

    public List<DatasourceInstanceAsset> querySshKeyAssets(String username) {
        DsInstanceParam.DsInstanceQuery instanceQuery = DsInstanceParam.DsInstanceQuery.builder()
                .instanceType(DsTypeEnum.GITLAB.getName())
                .build();

        List<DatasourceInstance> instances = dsInstancService.queryByParam(instanceQuery);
        List<DatasourceInstanceAsset> result = Lists.newArrayList();
        instances.forEach(i -> {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .assetType(DsAssetTypeEnum.GITLAB_SSHKEY.getType())
                    .instanceUuid(i.getUuid())
                    .name(username)
                    .build();
            result.addAll(dsInstanceAssetService.queryAssetByAssetParam(asset));
        });
        return result;
    }

}
