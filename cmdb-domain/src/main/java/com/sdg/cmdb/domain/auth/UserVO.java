package com.sdg.cmdb.domain.auth;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.sdg.cmdb.domain.ci.CiAppVO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.ss.SsVO;
import com.sdg.cmdb.domain.ssh.SshKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/9/22.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserVO implements Serializable {
    private static final long serialVersionUID = 4082571931463961821L;

    private long id;
    private String mail;
    private String displayName;
    private String username;
    private String mobile;
    private String token;
    private List<ResourceDO> resourceDOList;
    private RoleDO roleDO;
    // 所有角色
    private List<RoleDO> roleDOList;
    private List<ServerGroupDO> groupDOList;
    private List<SsVO> ssList;
    private int authed = 0; // 0：未授权；1：已授权
    // succeed   failed
    private String loginResult;
    private String userDN;
    private int accountLocked = 0; // 账户是否被锁定
    private int ldap = 0; // 0：未绑定；1：已绑定
    private List<LdapGroup> ldapGroups;
    private String userpassword; // 登录密码
    private String pwd;  // 其它密码
    private String rsaKey;  // 公钥
    private SshKey sshKey;
    private boolean setKeyByJms = false; // 推送到JMS
    private boolean setKeyByGitlab = false; // 推送到Gitlab
    private boolean isDev = false;
    private boolean safe;

    private List<ListPoliciesForUserResponse.Policy> policyList;  // RAM策略列表
    private List<ListAccessKeysResponse.AccessKey> accessKeyList;  // AK列表
    private List<CiAppVO> ciAppList;

    private String gmtCreate;

    public UserVO() {
    }

    public UserVO(UserDO userDO, RoleDO roleDO) {
        this.mail = userDO.getMail();
        this.displayName = userDO.getDisplayName();
        this.username = userDO.getUsername();
        this.mobile = userDO.getMobile();
        this.token = userDO.getToken();
        this.resourceDOList = userDO.getResourceDOList();
        this.pwd = userDO.getPwd();
        this.rsaKey = userDO.getRsaKey();
        this.gmtCreate = userDO.getGmtCreate();
        this.roleDO = roleDO;
    }


    public UserVO(UserDO userDO, List<ServerGroupDO> groupDOList) {
        this.id = userDO.getId();
        this.mail = userDO.getMail();
        this.displayName = userDO.getDisplayName();
        this.username = userDO.getUsername();
        this.mobile = userDO.getMobile();
        this.token = userDO.getToken();
        this.resourceDOList = userDO.getResourceDOList();
        this.pwd = userDO.getPwd();
        this.rsaKey = userDO.getRsaKey();
        this.gmtCreate = userDO.getGmtCreate();
        this.groupDOList = groupDOList;
    }

    public UserVO(UserDO userDO) {
        this.id = userDO.getId();
        this.mail = userDO.getMail();
        this.displayName = userDO.getDisplayName();
        this.username = userDO.getUsername();
        this.mobile = userDO.getMobile();
        this.token = userDO.getToken();
        this.resourceDOList = userDO.getResourceDOList();
        this.pwd = userDO.getPwd();
        this.rsaKey = userDO.getRsaKey();
        this.gmtCreate = userDO.getGmtCreate();
        this.authed = userDO.getAuthed();
    }

    public UserVO(UserDO userDO, boolean safe) {
        this.id = userDO.getId();
        this.mail = userDO.getMail();
        this.displayName = userDO.getDisplayName();
        if (!StringUtils.isEmpty(userDO.getMobile()))
            this.mobile = userDO.getMobile();
        this.username = userDO.getUsername();
        this.safe = safe;
        this.gmtCreate = userDO.getGmtCreate();

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
