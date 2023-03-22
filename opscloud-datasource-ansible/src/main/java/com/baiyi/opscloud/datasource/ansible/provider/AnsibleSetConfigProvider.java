package com.baiyi.opscloud.datasource.ansible.provider;

import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.constants.enums.CredentialKindEnum;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * Ansible配置文件推送(privateKey)
 *
 * @Author baiyi
 * @Date 2021/8/16 5:24 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AnsibleSetConfigProvider extends AbstractSetDsInstanceConfigProvider<AnsibleConfig.Ansible> {

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ANSIBLE.name();
    }

    @Override
    protected AnsibleConfig.Ansible buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, AnsibleConfig.class).getAnsible();
    }

    @Override
    protected void doSet(DsInstanceContext dsInstanceContext) {
        AnsibleConfig.Ansible ansible = buildConfig(dsInstanceContext.getDsConfig());
        // 取配置文件路径
        ansible.getPrivateKey();
        final String privateKeyPath = SystemEnvUtil.renderEnvHome(ansible.getPrivateKey());
        // 写入文件增加一个换行符号
        final String privateKey = getPrivateKey(dsInstanceContext) + "\n";
        IOUtil.writeFile(privateKey, privateKeyPath);
        // 修改文件权限
        try {
            setPrivateKeyPermissions(privateKeyPath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置密钥文件权限 600
     *
     * @param privateKey
     * @throws IOException
     */
    private void setPrivateKeyPermissions(String privateKey) throws IOException {
        //using PosixFilePermission to set file permissions 600
        Set<PosixFilePermission> perms = Sets.newHashSet();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        // perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
//        perms.add(PosixFilePermission.GROUP_READ);
//        perms.add(PosixFilePermission.GROUP_WRITE);
//        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
//        perms.add(PosixFilePermission.OTHERS_READ);
//        perms.add(PosixFilePermission.OTHERS_WRITE);
//        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        Files.setPosixFilePermissions(Paths.get(privateKey), perms);
    }

    private String getPrivateKey(DsInstanceContext dsInstanceContext) {
        if (dsInstanceContext.getDsConfig().getCredentialId() == null) {
            throw new OCException("凭据没有配置！");
        }
        //   CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind();
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        if (credential == null) {
            throw new OCException("凭据不存在！");
        }
        if (credential.getKind() == CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind() ||
                credential.getKind() == CredentialKindEnum.SSH_USERNAME_WITH_PRIVATE_KEY.getKind()) {
            return stringEncryptor.decrypt(credential.getCredential());
        } else {
            throw new OCException("凭据类型不符！");
        }
    }

}
