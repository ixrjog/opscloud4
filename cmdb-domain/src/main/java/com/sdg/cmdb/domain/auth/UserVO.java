package com.sdg.cmdb.domain.auth;

import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/9/22.
 */
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

    //  翻墙端口 用户ID+10000
    private String shadowsocksPort;

    /*
     0：未授权；1：已授权
    */
    private int authed = 0;

    // succeed   failed
    private String loginResult;

    private String mailAccountStatus;

    private String userDN;
    /**
     * 账户是否被锁定
     */
    private int accountLocked = 0;

    /*
     0：未绑定；1：已绑定
    */
    private int ldap = 0;

    // ldap组名称
    private List<String> ldapGroups;

    private List<LdapGroupVO> bambooLdapGroups;

    private List<LdapGroupVO> otherLdapGroups;

    private UserLdapGroupVO userLdapGroupVO;

    // 持续集成用户组
    private List<CiUserVO> ciUsers;

    /*
    登录密码
     */
    private String pwd;

    /*
    公钥
     */
    private String rsaKey;

    private String gmtCreate;

    private String shadowsocksServer1;

    private String shadowsocksServer2;

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
        this.shadowsocksPort = String.valueOf(userDO.getId() + 20000l);
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
        this.shadowsocksPort = String.valueOf(userDO.getId() + 20000l);
        this.groupDOList = groupDOList;
    }

    public UserVO(UserDO userDO) {
        this.id =userDO.getId();
        this.mail = userDO.getMail();
        this.displayName = userDO.getDisplayName();
        this.username = userDO.getUsername();
        this.mobile = userDO.getMobile();
        this.token = userDO.getToken();
        this.resourceDOList = userDO.getResourceDOList();
        this.pwd = userDO.getPwd();
        this.rsaKey = userDO.getRsaKey();
        this.gmtCreate = userDO.getGmtCreate();
        this.shadowsocksPort = String.valueOf(userDO.getId() + 20000l);
        this.authed = userDO.getAuthed();
    }

    public UserVO(UserDO userDO,boolean safe) {
        this.id =userDO.getId();
        this.mail = userDO.getMail();
        this.displayName = userDO.getDisplayName();
        this.username = userDO.getUsername();
        this.gmtCreate = userDO.getGmtCreate();
    }


    public UserLdapGroupVO getUserLdapGroupVO() {
        return userLdapGroupVO;
    }

    public void setUserLdapGroupVO(UserLdapGroupVO userLdapGroupVO) {
        this.userLdapGroupVO = userLdapGroupVO;
    }

    public List<LdapGroupVO> getOtherLdapGroups() {
        return otherLdapGroups;
    }

    public void setOtherLdapGroups(List<LdapGroupVO> otherLdapGroups) {
        this.otherLdapGroups = otherLdapGroups;
    }

    public List<LdapGroupVO> getBambooLdapGroups() {
        return bambooLdapGroups;
    }

    public void setBambooLdapGroups(List<LdapGroupVO> bambooLdapGroups) {
        this.bambooLdapGroups = bambooLdapGroups;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getShadowsocksPort() {
        return shadowsocksPort;
    }

    public void setShadowsocksPort(String shadowsocksPort) {
        this.shadowsocksPort = shadowsocksPort;
    }

    public String getShadowsocksServer1() {
        return shadowsocksServer1;
    }

    public void setShadowsocksServer1(String shadowsocksServer1) {
        this.shadowsocksServer1 = shadowsocksServer1;
    }

    public String getShadowsocksServer2() {
        return shadowsocksServer2;
    }

    public void setShadowsocksServer2(String shadowsocksServer2) {
        this.shadowsocksServer2 = shadowsocksServer2;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ResourceDO> getResourceDOList() {
        return resourceDOList;
    }

    public void setResourceDOList(List<ResourceDO> resourceDOList) {
        this.resourceDOList = resourceDOList;
    }

    public RoleDO getRoleDO() {
        return roleDO;
    }

    public void setRoleDO(RoleDO roleDO) {
        this.roleDO = roleDO;
    }

    public List<ServerGroupDO> getGroupDOList() {
        return groupDOList;
    }

    public void setGroupDOList(List<ServerGroupDO> groupDOList) {
        this.groupDOList = groupDOList;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

    public String getMailAccountStatus() {
        return mailAccountStatus;
    }

    public void setMailAccountStatus(String mailAccountStatus) {
        this.mailAccountStatus = mailAccountStatus;
    }

    public int getLdap() {
        return ldap;
    }

    public void setLdap(int ldap) {
        this.ldap = ldap;
    }

    public List<String> getLdapGroups() {
        return ldapGroups;
    }

    public void setLdapGroups(List<String> ldapGroups) {
        this.ldapGroups = ldapGroups;
    }

    public int getAuthed() {
        return authed;
    }

    public void setAuthed(int authed) {
        this.authed = authed;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUserDN() {
        return userDN;
    }

    public void setUserDN(String userDN) {
        this.userDN = userDN;
    }

    public int getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(int accountLocked) {
        this.accountLocked = accountLocked;
    }

    public List<RoleDO> getRoleDOList() {
        return roleDOList;
    }

    public void setRoleDOList(List<RoleDO> roleDOList) {
        this.roleDOList = roleDOList;
    }

    public List<CiUserVO> getCiUsers() {
        return ciUsers;
    }

    public void setCiUsers(List<CiUserVO> ciUsers) {
        this.ciUsers = ciUsers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "mail='" + mail + '\'' +
                ", displayName='" + displayName + '\'' +
                ", username='" + username + '\'' +
                ", loginResult='" + loginResult + '\'' +
                ", mobile='" + mobile + '\'' +
                ", token='" + token + '\'' +
                ", resourceDOList=" + resourceDOList +
                ", roleDO=" + roleDO +
                ", groupDOList=" + groupDOList +
                ", pwd='" + pwd + '\'' +
                ", rsaKey='" + rsaKey + '\'' +
                ", shadowsocksPort='" + shadowsocksPort + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }
}
