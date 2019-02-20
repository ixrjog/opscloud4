package com.sdg.cmdb.domain.ldap;

import com.sdg.cmdb.domain.auth.UserVO;

import java.io.Serializable;
import java.util.List;

public class LdapGroupVO extends LdapGroupDO implements Serializable {
    private static final long serialVersionUID = 6029699749764327265L;

    public LdapGroupVO() {

    }

    public LdapGroupVO(LdapGroupDO ldapGroupDO) {
        setId(ldapGroupDO.getId());
        setCn(ldapGroupDO.getCn());
        setContent(ldapGroupDO.getContent());
        setGroupType(ldapGroupDO.getGroupType());
        setWorkflow(ldapGroupDO.isWorkflow());
    }

    public LdapGroupVO(LdapGroupDO ldapGroupDO, List<UserVO> userList) {
        setId(ldapGroupDO.getId());
        setCn(ldapGroupDO.getCn());
        setContent(ldapGroupDO.getContent());
        setGroupType(ldapGroupDO.getGroupType());
        setWorkflow(ldapGroupDO.isWorkflow());
        this.userList = userList;
    }

    private List<UserVO> userList;

    public List<UserVO> getUserList() {
        return userList;
    }

    public void setUserList(List<UserVO> userList) {
        this.userList = userList;
    }
}
