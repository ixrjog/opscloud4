package com.sdg.cmdb.domain.auth;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.ss.SsVO;
import com.sdg.cmdb.domain.ssh.SshKey;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/9/22.
 */
@Data
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

    /*
     0：未授权；1：已授权
    */
    private int authed = 0;

    // succeed   failed
    private String loginResult;

    private String userDN;
    /**
     * 账户是否被锁定
     */
    private int accountLocked = 0;

    /*
     0：未绑定；1：已绑定
    */
    private int ldap = 0;

    private List<LdapGroup> ldapGroups;

    /**
     * 登录密码
     */
    private String userpassword;

    /**
     * 其它密码
     */
    private String pwd;

    /*
    公钥
     */
    private String rsaKey;

    private SshKey sshKey;

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
        this.gmtCreate = userDO.getGmtCreate();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
