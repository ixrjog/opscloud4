package com.baiyi.opscloud.account.impl;


import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.domain.generator.OcServerGroup;
import com.baiyi.opscloud.domain.generator.OcServerGroupPermission;
import com.baiyi.opscloud.domain.generator.OcUser;
import com.baiyi.opscloud.service.OcServerGroupPermissionService;
import com.baiyi.opscloud.service.OcServerGroupService;
import com.baiyi.opscloud.service.OcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2019/12/31 1:43 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseAccount implements InitializingBean, IAccount {

    public static final int PASSWORD_LENGTH = 16; // 初始密码长度

    @Resource
    protected OcUserService ocUserService;

    @Resource
    protected OcServerGroupPermissionService ocServerGroupPermissionService;

    @Resource
    protected OcServerGroupService ocServerGroupService;

    private Boolean saveOcUserList(List<OcUser> ocUserList) {
        Boolean result = true;
        for (OcUser ocUser : ocUserList) {
            if (!saveOcUser(ocUser))
                result = false;
        }
        return result;
    }

    /**
     * 只更新ldap源，其它源只添加条目
     *
     * @param ocUser
     * @return
     */
    private Boolean saveOcUser(OcUser ocUser) {
        try {
            if (ocUser.getId() != null && ocUser.getId() != 0) {
                if (!StringUtils.isEmpty(ocUser.getSource()) && ocUser.getSource().equals("ldap")) {
                    ocUserService.updateOcUser(ocUser);
                } else {
                    return true;
                }
            } else {
                OcUser checkUser = ocUserService.queryOcUserByUsername(ocUser.getUsername());
                if (checkUser == null) {
                    ocUserService.addOcUser(ocUser);
                } else {
                    ocUser.setId(checkUser.getId());
                    return saveOcUser(ocUser);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 全量同步
     *
     * @return
     */
    @Override
    public Boolean sync() {
        List<OcUser> userList = getUserList();
        return saveOcUserList(userList);
    }

    protected abstract List<OcUser> getUserList();

    /**
     * 异步任务
     *
     * @return
     */
    @Override
    @Async
    public void async() {
        sync();
    }





    /**
     * 移除
     *
     * @return
     */
    abstract public Boolean delete(OcUser user);

    /**
     * 更新
     *
     * @return
     */
    public Boolean update(OcUser user) {
        return true;
    }


    public Boolean isServerGroupResource() {
        return false;
    }

    /**
     * 授权
     * @param user
     * @param resource
     * @return
     */
    public Boolean grant(OcUser user, String resource) {
        return Boolean.TRUE;
    }

    /**
     * 吊销
     * @param user
     * @param resource
     * @return
     */
    public Boolean revoke(OcUser user, String resource) {
        return Boolean.TRUE;
    }

    /**
     * 推送用户公钥 PubKey
     *
     * @param user
     * @return
     */
    public Boolean pushSSHKey(OcUser user) {
        return Boolean.TRUE;
    }

    protected Boolean doPushKey(OcUser user) {
        return Boolean.TRUE;
    }

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    /**
     * 查询用户的服务器组授权
     *
     * @param ocUser
     * @return
     */
    protected List<OcServerGroup> queryUserServerGroupPermission(OcUser ocUser) {
        if (ocUser.getId() == null)
            ocUser = ocUserService.queryOcUserByUsername(ocUser.getUsername());
        List<OcServerGroupPermission> permissionList = ocServerGroupPermissionService.queryUserServerGroupPermission(ocUser.getId());
        if (permissionList.isEmpty()) return Collections.emptyList();
        return permissionList.stream().map(e -> {
            return ocServerGroupService.queryOcServerGroupById(e.getServerGroupId());
        }).collect(Collectors.toList());
    }


    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        AccountFactory.register(this);
    }


}
