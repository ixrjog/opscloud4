package com.sdg.cmdb.service;

import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.aliyun.AliyunRamPolicyDO;
import com.sdg.cmdb.domain.aliyun.AliyunRamUserDO;
import com.sdg.cmdb.domain.aliyun.AliyunRamUserVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMGroup;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMPolicy;

import java.util.List;

public interface AliyunRAMService {

    /**
     * 创建阿里云RAM子账户
     *
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> createUserByName(String username);

    /**
     * 创建阿里云RAM子账户
     *
     * @param userDO
     * @param createLoginProfile 是否开启控制台登录
     * @return
     */
    boolean createUser(UserDO userDO, boolean createLoginProfile);


    List<TodoDetailRAMGroup> getUserGroupInfo(UserDO userDO);


    boolean addUserToGroup(UserDO userDO, String groupName);

    List<ListPoliciesResponse.Policy> listPolicies(String policyType, int maxItems);

    /**
     * 同步策略
     *
     * @return
     */
    BusinessWrapper<Boolean> updateRamPolicies();

    /**
     * 查询用户策略
     *
     * @param username
     * @return
     */
    List<ListPoliciesForUserResponse.Policy> listPoliciesForUser(String username);

    BusinessWrapper<Boolean> setRamPolicyAllows(long id);

    TableVO<List<AliyunRamPolicyDO>> getPolicyPage(String policyName, String description, int page, int length);


    List<TodoDetailRAMPolicy> queryPolicy(String queryName);

    /**
     * 为用户撤销指定的授权
     *
     * @param username
     * @param todoDetailRAMPolicy
     * @return
     */
    boolean detachPolicyFromUser(String username, TodoDetailRAMPolicy todoDetailRAMPolicy);

    /**
     * 为指定用户附加授权
     *
     * @param username
     * @param todoDetailRAMPolicy
     * @return
     */
    boolean attachPolicyToUser(String username, TodoDetailRAMPolicy todoDetailRAMPolicy);


    List<ListUsersResponse.User> listAllUsers();


    ListUsersResponse listUsers(int maxItems, String marker);


    TableVO<List<AliyunRamUserVO>> getRamUserPage(String username, String userTag, int page, int length);

    AliyunRamUserDO getRamUser(long userId);

    /**
     * 导入策略
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> importRamUserPolicy(long id);

    BusinessWrapper<Boolean> saveRamUser(AliyunRamUserDO aliyunRamUserDO);

    BusinessWrapper<Boolean> updateRamUsers();


}
