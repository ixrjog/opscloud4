package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.LdapGroup;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.ldap.LdapGroupVO;
import com.sdg.cmdb.domain.nginx.VhostVO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailLdapGroup;

import java.util.List;

public interface LdapService {

    /**
     * 判断用户是否绑定
     *
     * @param username
     * @return
     */
    boolean checkUserInLdap(String username);

    /**
     * 更新用户信息 LDAP
     *
     * @param userVO
     * @return
     */
    BusinessWrapper<Boolean> updateUser(UserVO userVO);

    /**
     * LDAP新建Group
     *
     * @param username
     * @param groupname
     * @return
     */
    BusinessWrapper<Boolean> addLdapGroup(String username, String groupname);

    boolean isGroupMember(String username, String groupName);

    BusinessWrapper<Boolean> delLdapGroup(String username, String groupname);


    List<String> searchLdapGroup();

    List<String> searchBambooGroup();

    /**
     * 获取指定用户名的对象
     *
     * @param username
     * @return
     */
    UserDO getUserByName(String username);

    /**
     * 查询用户的所有ldap组
     *
     * @return
     */
    List<String> searchLdapGroup(String username);

    List<LdapGroup> searchUserLdapGroup(String username);

    List<UserVO>  searchLdapGroupUsers(String groupname);


    /**
     * 查询工作流的LDAP组权限
     * @param username
     * @return
     */
    List<TodoDetailLdapGroup> getUserWorkflowLdapGroup(String username);

    /**
     * 移除group中的用户
     *
     * @param username
     * @param groupName
     * @return
     */
    boolean removeMember2Group(String username, String groupName);

    BusinessWrapper<Boolean> delUserToGroup(String groupName,String username);

    /**
     * unbind 用户所在的组
     *
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> removeMember(String username);

    boolean delUser(String username);

    boolean checkUserInLdapGroup(UserDO userDO, ServerGroupDO serverGroupDO);

    boolean addMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO);

    boolean addMemberToGroup(UserDO userDO, String groupName);

    /**
     *
     * @param groupName  LDAP group
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> addUserToGroup(String groupName,String username);

    boolean delMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO);

    boolean delMemberToGroup(UserDO userDO, String groupName);

    //List<String> searchLdapGroupUser(String ldapGroupName);

    // List<String> searchBambooGroupFilter(String username);
    BusinessWrapper<Boolean> createLdapGroup(String groupName, String content, int groupType);

    BusinessWrapper<Boolean> delLdapGroup(long id);

    boolean addLdapGroup(String cn);

    TableVO<List<LdapGroupVO>> getLdapGroupPage(String cn, int groupType, int page, int length);

    LdapGroupVO getLdapGroup(String cn);

    boolean addUser(UserVO userVO);

    BusinessWrapper<Boolean> unbindUser(String username);

}
