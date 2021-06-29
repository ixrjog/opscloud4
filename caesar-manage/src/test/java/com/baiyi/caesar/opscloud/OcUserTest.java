package com.baiyi.caesar.opscloud;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserCredential;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.facade.user.UserCredentialFacade;
import com.baiyi.caesar.opscloud.provider.OcUserProvider;
import com.baiyi.caesar.opscloud.vo.OcUserVO;
import com.baiyi.caesar.service.server.ServerGroupService;
import com.baiyi.caesar.service.user.UserCredentialService;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/23 5:22 下午
 * @Version 1.0
 */
public class OcUserTest extends BaseUnit {

    @Resource
    private OcUserProvider userProvider;

    @Resource
    private UserService userService;

    @Resource
    private UserCredentialFacade userCredentialFacade;

    @Resource
    private UserCredentialService userCredentialService;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private ServerGroupService serverGroupService;

    @Test
    void syncUsers() {
        try {
            DataTable<UserVO.User> table = userProvider.queryUsers();
            for (UserVO.User userVO : table.getData()) {
                userVO.setPassword("0");
                User user = userService.getByUsername(userVO.getUsername());
                if (user == null) {
                    user = BeanCopierUtil.copyProperties(userVO, User.class);
                    user.setId(null);
                    userService.add(user);
                }
                OcUserVO.User ocUser = userProvider.queryUserDetail(user.getUsername());
                if (ocUser != null) {
                    if (ocUser.getCredentialMap() != null) {
                        if (ocUser.getCredentialMap().containsKey("sshPubKey")) {
                            UserCredentialVO.Credential credential = UserCredentialVO.Credential.builder()
                                    .credential(ocUser.getCredentialMap().get("sshPubKey").getCredential())
                                    .credentialType(0)
                                    .userId(user.getId())
                                    .build();
                            saveUserCredential(user, credential);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveUserCredential(User user, UserCredentialVO.Credential credential) {
        List<UserCredential> list = userCredentialService.queryByUserIdAndType(credential.getUserId(), credential.getCredentialType());
        if (CollectionUtils.isEmpty(list))
            userCredentialFacade.saveUserCredential(credential, user);
    }


    @Test
    void syncUserPermission() {
        List<User> userList = userService.listActive();
        userList.forEach(user -> {
            try {
                OcUserVO.User ocUser = userProvider.queryUserDetail(user.getUsername());
                List<ServerGroupVO.ServerGroup> serverGroups = ocUser.getServerGroups();
                if (!CollectionUtils.isEmpty(serverGroups)) {
                    serverGroups.forEach(serverGroup -> {
                        ServerGroup group = serverGroupService.getByName(serverGroup.getName());
                        UserPermission userPermission = UserPermission.builder()
                                .userId(user.getId())
                                .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                                .businessId(group.getId())
                                .build();
                        if (serverGroup.getIsAdmin())
                            userPermission.setPermissionRole("admin");
                        if (userPermissionService.getByUserPermission(userPermission) == null) {
                            try {
                                userPermissionService.add(userPermission);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
