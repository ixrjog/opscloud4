package com.baiyi.opscloud.cloud.ram.impl;

import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ram.handler.AliyunRAMUserHandler;
import com.baiyi.opscloud.cloud.ram.AliyunRAMUserCenter;
import com.baiyi.opscloud.cloud.ram.builder.AliyunRamUserBuilder;
import com.baiyi.opscloud.cloud.ram.handler.AliyunRAMUserPolicyPermissionHandler;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.RAMType;
import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamUserService;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/6/10 11:33 上午
 * @Version 1.0
 */
@Component
public class AliyunRAMUserCenterImpl implements AliyunRAMUserCenter {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunRAMUserHandler aliyunRAMUserHandler;

    @Resource
    private OcAliyunRamUserService ocAliyunRamUserService;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    @Resource
    private AliyunRAMUserPolicyPermissionHandler aliyunRAMUserPolicyPermissionHandler;

    @Resource
    private OcUserService ocUserService;

    public static final boolean CREATE_LOGIN_PROFILE = true;

    @Override
    public BusinessWrapper<OcAliyunRamUser> createRamUser(String accountUid, OcUser ocUser) {
        // 查询账户是否存在
        List<OcAliyunRamUser> ramUserList =
                ocAliyunRamUserService.queryUserPermissionRamUserByUserId(ocUser.getId()).stream().filter(e -> e.getAccountUid().equals(accountUid)).collect(Collectors.toList());
        if (!ramUserList.isEmpty())
            return new BusinessWrapper<>(ramUserList.get(0));
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(accountUid);
        BusinessWrapper<GetUserResponse.User> getUserWrapper = aliyunRAMUserHandler.getRamUser(aliyunAccount, ocUser);
        if (getUserWrapper.isSuccess())
            return new BusinessWrapper<>(addRamUser(aliyunAccount, getUserWrapper.getBody()));
        BusinessWrapper<CreateUserResponse.User> createUserWrapper = aliyunRAMUserHandler.createRamUser(aliyunAccount, ocUser, CREATE_LOGIN_PROFILE);
        if (!createUserWrapper.isSuccess())
            return new BusinessWrapper<>(createUserWrapper.getCode(), createUserWrapper.getDesc());
        return new BusinessWrapper<>(addRamUser(aliyunAccount, createUserWrapper.getBody()));
    }

    @Override
    public BusinessWrapper<Boolean> deleteRamUser(OcAliyunRamUser ocAliyunRamUser) {
        // 删除账户绑定关系
        unbindUserPermission(ocAliyunRamUser);
        // 删除策略关系
        aliyunRAMUserPolicyPermissionHandler.deleteOcAliyunRamPermissionByOcAliyunRamUser(ocAliyunRamUser);
        // 删除账户
        ocAliyunRamUserService.deleteOcAliyunRamUserById(ocAliyunRamUser.getId());
        return BusinessWrapper.SUCCESS;
    }

    private OcAliyunRamUser addRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, GetUserResponse.User user) {
        OcAliyunRamUser ocAliyunRamUser = AliyunRamUserBuilder.build(aliyunAccount, user);
        saveRamUser(aliyunAccount, ocAliyunRamUser);
        return ocAliyunRamUser;
    }

    private OcAliyunRamUser addRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, CreateUserResponse.User user) {
        OcAliyunRamUser ocAliyunRamUser = AliyunRamUserBuilder.build(aliyunAccount, user);
        saveRamUser(aliyunAccount, ocAliyunRamUser);
        return ocAliyunRamUser;
    }


    @Override
    public List<ListUsersResponse.User> getUsers(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        return aliyunRAMUserHandler.getUsers(aliyunAccount);
    }

    @Override
    public BusinessWrapper<Boolean> syncUsers() {
        aliyunCore.getAccounts().forEach(e -> {
            Map<String, OcAliyunRamUser> ramUserMap = queryRamUserMap(e);
            List<ListUsersResponse.User> users = getUsers(e);
            syncUsers(e, users, ramUserMap);
            deleteUsers(ramUserMap); // 清理不存在的账户
        });
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncUser(OcAliyunRamUser ocAliyunRamUser) {
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(ocAliyunRamUser.getAccountUid());
        aliyunRAMUserPolicyPermissionHandler.syncUserPolicyPermission(aliyunAccount, ocAliyunRamUser);
        return BusinessWrapper.SUCCESS;
    }

    private void syncUsers(AliyunCoreConfig.AliyunAccount aliyunAccount, List<ListUsersResponse.User> users, Map<String, OcAliyunRamUser> ramUserMap) {
        if (users == null) return;
        users.forEach(e -> {
            OcAliyunRamUser pre = AliyunRamUserBuilder.build(aliyunAccount, e);
            if (ramUserMap.containsKey(pre.getRamUsername())) {
                OcAliyunRamUser ocAliyunRamUser = ramUserMap.get(pre.getRamUsername());
                pre.setId(ocAliyunRamUser.getId());
                pre.setRamType(ocAliyunRamUser.getRamType());
                ramUserMap.remove(pre.getRamUsername());
            }
            saveRamUser(aliyunAccount, pre);
        });
    }

    private void saveRamUser(AliyunCoreConfig.AliyunAccount aliyunAccount, OcAliyunRamUser ocAliyunRamUser) {
        List<ListAccessKeysResponse.AccessKey> keys = aliyunRAMUserHandler.getUserAccessKeys(aliyunAccount, ocAliyunRamUser.getRamUsername());
        ocAliyunRamUser.setAccessKeys(keys.isEmpty() ? 0 : keys.size());
        if (IDUtils.isEmpty(ocAliyunRamUser.getId())) {
            ocAliyunRamUserService.addOcAliyunRamUser(ocAliyunRamUser);
        } else {
            ocAliyunRamUserService.updateOcAliyunRamUser(ocAliyunRamUser);
        }
        bindUserPermission(ocAliyunRamUser);
        aliyunRAMUserPolicyPermissionHandler.syncUserPolicyPermission(aliyunAccount, ocAliyunRamUser);
    }

    private void unbindUserPermission(OcAliyunRamUser ocAliyunRamUser) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(ocAliyunRamUser.getRamUsername());
        if (ocUser == null) return;
        OcUserPermission pre = new OcUserPermission();
        pre.setUserId(ocUser.getId());
        pre.setBusinessType(BusinessType.ALIYUN_RAM_ACCOUNT.getType());
        pre.setBusinessId(ocAliyunRamUser.getId());
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(pre);
        if (ocUserPermission != null)
            ocUserPermissionService.delOcUserPermissionById(ocUserPermission.getId());
    }


    private void bindUserPermission(OcAliyunRamUser ocAliyunRamUser) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(ocAliyunRamUser.getRamUsername());
        if (ocUser == null) return;
        OcUserPermission pre = new OcUserPermission();
        pre.setUserId(ocUser.getId());
        pre.setBusinessType(BusinessType.ALIYUN_RAM_ACCOUNT.getType());
        pre.setBusinessId(ocAliyunRamUser.getId());
        OcUserPermission ocUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(pre);
        if (ocUserPermission == null)
            ocUserPermissionService.addOcUserPermission(pre);
        if (ocAliyunRamUser.getRamType() == RAMType.DEFAULT.getType()) {
            ocAliyunRamUser.setRamType(RAMType.USER.getType());
            ocAliyunRamUserService.updateOcAliyunRamUser(ocAliyunRamUser);
        }
    }

    private void deleteUsers(Map<String, OcAliyunRamUser> ramUserMap) {
        ramUserMap.keySet().forEach(k -> {
            OcAliyunRamUser ocAliyunRamUser = ramUserMap.get(k);
            deletetUserPermission(ocAliyunRamUser.getId());
            ocAliyunRamUserService.deleteOcAliyunRamUserById(ocAliyunRamUser.getId());
        });
    }

    private void deletetUserPermission(int businessId) {
        List<OcUserPermission> list = ocUserPermissionService.queryUserBusinessPermissionByBusinessId(BusinessType.ALIYUN_RAM_ACCOUNT.getType(), businessId);
        list.forEach(e -> ocUserPermissionService.delOcUserPermissionById(e.getId()));
    }

    private Map<String, OcAliyunRamUser> queryRamUserMap(AliyunCoreConfig.AliyunAccount aliyunAccount) {
        Map<String, OcAliyunRamUser> ramUserMap = Maps.newHashMap();
        List<OcAliyunRamUser> ramUsers = ocAliyunRamUserService.queryOcAliyunRamUserByAccountUid(aliyunAccount.getUid());
        ramUsers.forEach(e -> ramUserMap.put(e.getRamUsername(), e));
        return ramUserMap;
    }


}
