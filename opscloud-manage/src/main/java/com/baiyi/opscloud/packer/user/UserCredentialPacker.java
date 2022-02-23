package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/9 11:32 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserCredentialPacker implements IWrapper<UserVO.User> {

    private final UserCredentialService userCredentialService;

    private final DsInstanceAssetFacade dsInstanceAssetFacade;

    @Override
    public void wrap(UserVO.User user, IExtend iExtend) {
        List<UserCredential> userCredentials = userCredentialService.queryByUserId(user.getId());
        UserCredentialVO.CredentialDetails credentialDetails = UserCredentialVO.CredentialDetails.builder()
                .credentialMap(buildCredentialMap(userCredentials))
                .assetCredentialMap(buildAssetCredentialMap(user.getUsername()))
                .build();
        user.setCredentialDetails(credentialDetails);
    }

    private Map<String, List<UserCredentialVO.Credential>> buildCredentialMap(List<UserCredential> userCredentials) {
        Map<String, List<UserCredentialVO.Credential>> credentialMap = Maps.newConcurrentMap();
        userCredentials.forEach(e -> {
            String typeName = UserCredentialTypeEnum.getName(e.getCredentialType());
            UserCredentialVO.Credential credential = BeanCopierUtil.copyProperties(e, UserCredentialVO.Credential.class);
            if (credentialMap.containsKey(typeName)) {
                credentialMap.get(typeName).add(credential);
            } else {
                credentialMap.put(typeName, Lists.newArrayList(credential));
            }
        });
        return credentialMap;
    }

    private Map<String, List<DsAssetVO.Asset>> buildAssetCredentialMap(String username) {
        Map<String, List<DsAssetVO.Asset>> assetCredentialMap = Maps.newHashMap();
        assetCredentialMap.put(DsAssetTypeConstants.GITLAB_SSHKEY.name(), dsInstanceAssetFacade.querySshKeyAssets(username));
        return assetCredentialMap;
    }

}
