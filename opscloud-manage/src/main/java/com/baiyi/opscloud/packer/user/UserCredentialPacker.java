package com.baiyi.opscloud.packer.user;

import com.baiyi.opscloud.common.type.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/9 11:32 上午
 * @Version 1.0
 */
@Component
public class UserCredentialPacker {

    @Resource
    private UserCredentialService userCredentialService;

    public void wrap(UserVO.User user) {
        List<UserCredential> userCredentials = userCredentialService.queryByUserId(user.getId());
        UserCredentialVO.CredentialDetails credentialDetails = UserCredentialVO.CredentialDetails.builder()
                .credentialMap(buildCredentialMap(userCredentials))
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
}
