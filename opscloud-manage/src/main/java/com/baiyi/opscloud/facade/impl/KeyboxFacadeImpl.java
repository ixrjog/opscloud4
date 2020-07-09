package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.SSHUtils;
import com.baiyi.opscloud.decorator.keybox.KeyboxDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcKeybox;
import com.baiyi.opscloud.domain.param.keybox.KeyboxParam;
import com.baiyi.opscloud.domain.vo.keybox.KeyboxVO;
import com.baiyi.opscloud.facade.KeyboxFacade;
import com.baiyi.opscloud.service.keybox.OcKeyboxService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:17 下午
 * @Version 1.0
 */
@Service
public class KeyboxFacadeImpl implements KeyboxFacade {

    @Resource
    private OcKeyboxService ocKeyboxService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private KeyboxDecorator keyboxDecorator;

    @Override
    public SSHKeyCredential getSSHKeyCredential(String systemUser) {
        OcKeybox ocKeybox = ocKeyboxService.queryOcKeyboxBySystemUser(systemUser);
        if (ocKeybox == null)
            return null;
        SSHKeyCredential key = SSHKeyCredential.builder()
                .systemUser(systemUser)
                .privateKey(stringEncryptor.decrypt(ocKeybox.getPrivateKey()))
                .publicKey(ocKeybox.getPublicKey())
                .passphrase(StringUtils.isEmpty(ocKeybox.getPassphrase()) ? "" : stringEncryptor.decrypt(ocKeybox.getPassphrase()))
                .build();
        return key;
    }

    @Override
    public DataTable<KeyboxVO.Keybox> queryKeyboxPage(KeyboxParam.PageQuery pageQuery) {
        DataTable<OcKeybox> table = ocKeyboxService.queryOcKeyboxByParam(pageQuery);
        List<KeyboxVO.Keybox> page = BeanCopierUtils.copyListProperties(table.getData(), KeyboxVO.Keybox.class);
        return new DataTable<>(page.stream().map(e -> keyboxDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addKeybox(KeyboxVO.Keybox keybox) {
        OcKeybox ocKeybox = BeanCopierUtils.copyProperties(keybox, OcKeybox.class);
        if (ocKeybox.getKeyType() == 0) { // sshKey
            if (StringUtils.isEmpty(ocKeybox.getPublicKey()))
                return new BusinessWrapper<>(ErrorEnum.KEYBOX_PUBLIC_KEY_IS_EMPTY);
            if (StringUtils.isEmpty(ocKeybox.getPrivateKey()))
                return new BusinessWrapper<>(ErrorEnum.KEYBOX_PRIVATE_KEY_IS_EMPTY);
            ocKeybox.setPrivateKey(stringEncryptor.encrypt(keybox.getPrivateKey()));
            if (!StringUtils.isEmpty(ocKeybox.getPassphrase()))
                ocKeybox.setPassphrase(stringEncryptor.encrypt(keybox.getPassphrase()));
            ocKeybox.setFingerprint(SSHUtils.getFingerprint(ocKeybox.getPublicKey()));
        } else { // user/password
            if (StringUtils.isEmpty(ocKeybox.getPassphrase()))
                return new BusinessWrapper<>(ErrorEnum.KEYBOX_PASSPHRASE_IS_EMPTY);
            ocKeybox.setPassphrase(stringEncryptor.encrypt(keybox.getPassphrase()));
        }
        ocKeyboxService.addOcKeybox(ocKeybox);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteKeyboxById(int id) {
        ocKeyboxService.deleteOcKeyboxById(id);
        return BusinessWrapper.SUCCESS;
    }

}
