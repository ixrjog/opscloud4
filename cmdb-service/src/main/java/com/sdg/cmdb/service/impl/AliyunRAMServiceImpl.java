package com.sdg.cmdb.service.impl;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.ram.model.v20150501.*;
import com.sdg.cmdb.dao.cmdb.AliyunDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.aliyun.AliyunRamPolicyDO;
import com.sdg.cmdb.domain.aliyun.AliyunRamUserDO;
import com.sdg.cmdb.domain.aliyun.AliyunRamUserVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.AliyunEcsItemEnum;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPDetailVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMGroup;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMPolicy;
import com.sdg.cmdb.service.AliyunRAMService;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.UserService;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.MobileUtil;
import com.sdg.cmdb.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AliyunRAMServiceImpl implements AliyunRAMService {

    @Resource
    private ConfigCenterService configCenterService;

    private HashMap<String, String> configMap;

    public static final String RAM_GROUP = "RG_";

    @Resource
    private UserDao userDao;

    @Resource
    private AliyunDao aliyunDao;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.ALIYUN_ECS.getItemKey());
    }


    @Override
    public BusinessWrapper<Boolean> createUserByName(String username
    ) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null)
            return new BusinessWrapper<Boolean>(true);
        return new BusinessWrapper<Boolean>(createUser(userDO, true));
    }


    @Override
    public boolean createUser(UserDO userDO, boolean createLoginProfile) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(userDO.getUsername());
        request.setDisplayName(userDO.getDisplayName());
        if (MobileUtil.isPhone(userDO.getMobile()))
            request.setMobilePhone("86-" + userDO.getMobile());
        if (!StringUtils.isEmpty(userDO.getMail()))
            request.setEmail(userDO.getMail());
        request.setComments("Created by opsCloud");
        CreateUserResponse response = sampleCreateUserResponse(EcsServiceImpl.regionIdCnHangzhou, request);

        // 判断是否成功创建用户
        if (response != null && response.getUser().getUserName().equals(userDO.getUsername())) {
            CreateUserResponse.User user = response.getUser();
            try {
                aliyunDao.addRamUser(new AliyunRamUserDO(response.getUser()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (createLoginProfile) {
                return createLoginProfile(userDO, false);
            } else {
                return true;
            }
        }
        return false;
    }

    private CreateUserResponse sampleCreateUserResponse(String regionId, CreateUserRequest request) {
        if (StringUtils.isEmpty(regionId))
            regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);

        try {
            CreateUserResponse response
                    = client.getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 开通控制台登录
     *
     * @param userDO
     * @param passwordResetRequired
     * @return
     */
    private boolean createLoginProfile(UserDO userDO, boolean passwordResetRequired) {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(userDO.getUsername());
        request.setPassword(userDO.getPwd());
        request.setPasswordResetRequired(passwordResetRequired);
        CreateLoginProfileResponse response = sampleCreateLoginProfileResponse(EcsServiceImpl.regionIdCnHangzhou, request);
        if (response != null && response.getLoginProfile().getUserName().equals(userDO.getUsername())) {
            return true;
        } else {
            return false;
        }

    }

    private CreateLoginProfileResponse sampleCreateLoginProfileResponse(String regionId, CreateLoginProfileRequest request) {
        if (StringUtils.isEmpty(regionId))
            regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);

        try {
            CreateLoginProfileResponse response
                    = client.getAcsResponse(request);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TodoDetailRAMGroup> getUserGroupInfo(UserDO userDO) {
        List<TodoDetailRAMGroup> groups = new ArrayList<>();

        GetUserResponse.User user = getUser(EcsServiceImpl.regionIdCnHangzhou, userDO);
        if (user == null)
            createUser(userDO, true);
        List<ListGroupsResponse.Group> ramGroups = listGroups(EcsServiceImpl.regionIdCnHangzhou);

        //用户的权限组
        List<ListGroupsForUserResponse.Group> ramUserGroups = listGroupsForUser(EcsServiceImpl.regionIdCnHangzhou, userDO);

        for (ListGroupsResponse.Group ramGroup : ramGroups) {
            if (ramGroup.getGroupName().indexOf(RAM_GROUP) != 0)
                continue;
            TodoDetailRAMGroup todoDetailRAMGroup = new TodoDetailRAMGroup(ramGroup.getGroupName(), ramGroup.getComments());
            for (ListGroupsForUserResponse.Group ramUserGroup : ramUserGroups) {
                if (ramUserGroup.getGroupName().equals(ramGroup.getGroupName())) {
                    todoDetailRAMGroup.setGroupValue("1");
                    break;
                }
            }
            groups.add(todoDetailRAMGroup);
        }
        return groups;
    }

    /**
     * 查询RAM子账户
     *
     * @param regionId
     * @param userDO
     * @return
     */
    private GetUserResponse.User getUser(String regionId, UserDO userDO) {
        GetUserRequest request = new GetUserRequest();
        request.setUserName(userDO.getUsername());
        if (StringUtils.isEmpty(regionId))
            regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            GetUserResponse response
                    = client.getAcsResponse(request);
            return response.getUser();

        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (com.aliyuncs.exceptions.ClientException e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询所有的组
     *
     * @param regionId
     * @return
     */
    private List<ListGroupsResponse.Group> listGroups(String regionId) {
        ListGroupsRequest request = new ListGroupsRequest();
        if (StringUtils.isEmpty(regionId))
            regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            ListGroupsResponse response
                    = client.getAcsResponse(request);
            return response.getGroups();
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询RAM子账户所属的用户组
     *
     * @param regionId
     * @param userDO
     * @return
     */
    private List<ListGroupsForUserResponse.Group> listGroupsForUser(String regionId, UserDO userDO) {
        ListGroupsForUserRequest request = new ListGroupsForUserRequest();
        request.setUserName(userDO.getUsername());
        if (StringUtils.isEmpty(regionId))
            regionId = EcsServiceImpl.regionIdCnHangzhou;
        IAcsClient client = acqIAcsClient(regionId);
        try {
            ListGroupsForUserResponse response
                    = client.getAcsResponse(request);
            return response.getGroups();
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addUserToGroup(UserDO userDO, String groupName) {
        AddUserToGroupRequest request = new AddUserToGroupRequest();
        request.setGroupName(groupName);
        request.setUserName(userDO.getUsername());

        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            AddUserToGroupResponse response
                    = client.getAcsResponse(request);
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询策略
     *
     * @param policyType 指定Policy的类型, 取值System或Custom，如果没有指定则列出所有授权策略
     * @param maxItems
     * @return
     */
    @Override
    public List<ListPoliciesResponse.Policy> listPolicies(String policyType, int maxItems) {
        if (maxItems == 0 || maxItems > 1000)
            maxItems = 1000;
        ListPoliciesRequest request = new ListPoliciesRequest();
        request.setMaxItems(maxItems);
        if (!StringUtils.isEmpty(policyType))
            request.setPolicyType(policyType);
        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            ListPoliciesResponse response
                    = client.getAcsResponse(request);
            return response.getPolicies();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public BusinessWrapper<Boolean> updateRamPolicies() {
        List<ListPoliciesResponse.Policy> list = listPolicies(null, 1000);
        for (ListPoliciesResponse.Policy policy : list) {
            AliyunRamPolicyDO policyDO = aliyunDao.queryRamPolicyByName(policy.getPolicyName());
            if (policyDO == null)
                try {
                    aliyunDao.addRamPolicy(new AliyunRamPolicyDO(policy));
                } catch (Exception e) {
                    return new BusinessWrapper<Boolean>(false);
                }
        }
        return new BusinessWrapper<Boolean>(true);
    }

    @Override
    public List<ListPoliciesForUserResponse.Policy> listPoliciesForUser(String username) {
        if (StringUtils.isEmpty(username))
            return new ArrayList<>();
        ListPoliciesForUserRequest request = new ListPoliciesForUserRequest();
        request.setUserName(username);

        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            ListPoliciesForUserResponse response
                    = client.getAcsResponse(request);
            return response.getPolicies();
        } catch (ServerException e) {
            // e.printStackTrace();
        } catch (ClientException e) {
            // e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public BusinessWrapper<Boolean> setRamPolicyAllows(long id) {
        AliyunRamPolicyDO aliyunRamPolicyDO = aliyunDao.getRamPolicy(id);
        if (aliyunRamPolicyDO == null) return new BusinessWrapper<Boolean>(false);
        try {
            aliyunRamPolicyDO.setAllows(!aliyunRamPolicyDO.isAllows());
            aliyunDao.updateRamPolicy(aliyunRamPolicyDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(false);
    }


    @Override
    public TableVO<List<AliyunRamPolicyDO>> getPolicyPage(String policyName, String description, int page, int length) {
        long size = aliyunDao.getPolicySize(policyName, description);
        List<AliyunRamPolicyDO> list = aliyunDao.getPolicyPage(policyName, description, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public List<TodoDetailRAMPolicy> queryPolicy(String queryName) {
        List<AliyunRamPolicyDO> list = aliyunDao.queryPolicyByWorkflow(queryName);
        List<TodoDetailRAMPolicy> todoDetailRAMPolicyList = new ArrayList<>();
        for (AliyunRamPolicyDO aliyunRamPolicyDO : list)
            todoDetailRAMPolicyList.add(new TodoDetailRAMPolicy(aliyunRamPolicyDO));
        return todoDetailRAMPolicyList;
    }

    @Override
    public boolean detachPolicyFromUser(String username, TodoDetailRAMPolicy todoDetailRAMPolicy) {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(username);
        request.setPolicyName(todoDetailRAMPolicy.getPolicyName());
        request.setPolicyType(todoDetailRAMPolicy.getPolicyType());

        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            DetachPolicyFromUserResponse response
                    = client.getAcsResponse(request);
            if (StringUtils.isEmpty(response.getRequestId()))
                return false;
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean attachPolicyToUser(String username, TodoDetailRAMPolicy todoDetailRAMPolicy) {
        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
        request.setUserName(username);
        request.setPolicyName(todoDetailRAMPolicy.getPolicyName());
        request.setPolicyType(todoDetailRAMPolicy.getPolicyType());

        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            AttachPolicyToUserResponse response
                    = client.getAcsResponse(request);
            if (StringUtils.isEmpty(response.getRequestId()))
                return false;
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<ListUsersResponse.User> listAllUsers() {
        ListUsersResponse response = listUsers(20, null);

        List<ListUsersResponse.User> users = new ArrayList<>();
        users.addAll(response.getUsers());
        if (!response.getIsTruncated())
            return users;
        String marker = response.getMarker();

        while (true) {
            ListUsersResponse responseMarker = listUsers(20, marker);
            users.addAll(responseMarker.getUsers());
            if (!responseMarker.getIsTruncated()) {
                return users;
            } else {
                marker = responseMarker.getMarker();
            }
        }
    }


    @Override
    public ListUsersResponse listUsers(int maxItems, String marker) {
        if (maxItems == 0 || maxItems > 100)
            maxItems = 100;
        ListUsersRequest request = new ListUsersRequest();
        request.setMaxItems(maxItems);
        if (!StringUtils.isEmpty(marker))
            request.setMarker(marker);
        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        try {
            ListUsersResponse response
                    = client.getAcsResponse(request);

            return response;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询用户激活的AccessKey
     *
     * @param username
     * @return
     */
    public List<ListAccessKeysResponse.AccessKey> listAccessKeys(String username) {
        ListAccessKeysRequest request = new ListAccessKeysRequest();
        request.setUserName(username);
        IAcsClient client = acqIAcsClient(EcsServiceImpl.regionIdCnHangzhou);
        List<ListAccessKeysResponse.AccessKey> keys = new ArrayList<>();
        try {
            ListAccessKeysResponse response
                    = client.getAcsResponse(request);
            for (ListAccessKeysResponse.AccessKey key : response.getAccessKeys()) {
                if (key.getStatus().equalsIgnoreCase("Active"))
                    keys.add(key);
            }
            return keys;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return keys;
    }

    @Override
    public TableVO<List<AliyunRamUserVO>> getRamUserPage(String username, String userTag, int page, int length) {
        long size = aliyunDao.getRamUserSize(username, userTag);
        List<AliyunRamUserDO> list = aliyunDao.getRamUserPage(username, userTag, page * length, length);
        List<AliyunRamUserVO> voList = new ArrayList<>();
        for (AliyunRamUserDO aliyunRamUserDO : list) {
            voList.add(getAliyunRamUserVO(aliyunRamUserDO));
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public AliyunRamUserDO getRamUser(long userId) {
        UserDO userDO = userDao.getUserById(userId);
        AliyunRamUserDO aliyunRamUserDO = aliyunDao.getRamUserByName(userDO.getUsername());
        if (aliyunRamUserDO != null)
            return (aliyunDao.getRamUserByName(userDO.getUsername()));
        return new AliyunRamUserDO(userDO);
    }

    @Override
    public BusinessWrapper<Boolean> importRamUserPolicy(long id) {
        try {
            AliyunRamUserDO aliyunRamUserDO = aliyunDao.getRamUser(id);
            // TODO 查询源策略
            List<ListPoliciesForUserResponse.Policy> policyList = listPoliciesForUser(aliyunRamUserDO.getRamUserName());

            UserDO userDO = userDao.getUserById(aliyunRamUserDO.getUserId());
            // TODO 先从数据库查询是否有用户
            AliyunRamUserDO ramUserDO = aliyunDao.getRamUserByName(userDO.getUsername());
            if (ramUserDO == null) {
                if (getUser(EcsServiceImpl.regionIdCnHangzhou, userDO) == null)
                    createUser(userDO, true);
            }
            List<ListPoliciesForUserResponse.Policy> policies = listPoliciesForUser(userDO.getUsername());
            HashMap<String, ListPoliciesForUserResponse.Policy> map = new HashMap<>();
            for (ListPoliciesForUserResponse.Policy policy : policies)
                map.put(policy.getPolicyName(), policy);

            for (ListPoliciesForUserResponse.Policy policy : policyList) {
                if (!map.containsKey(policy.getPolicyName()))
                    attachPolicyToUser(userDO.getUsername(), new TodoDetailRAMPolicy(policy));
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(false);
    }


    @Override
    public BusinessWrapper<Boolean> saveRamUser(AliyunRamUserDO aliyunRamUserDO) {
        try {
            if (aliyunRamUserDO.getId() == 0) {
                aliyunDao.addRamUser(aliyunRamUserDO);
            } else {
                aliyunDao.updateRamUser(aliyunRamUserDO);
            }
            return new BusinessWrapper<Boolean>(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(true);
    }

    private AliyunRamUserVO getAliyunRamUserVO(AliyunRamUserDO aliyunRamUserDO) {

        AliyunRamUserVO aliyunRamUserVO = BeanCopierUtils.copyProperties(aliyunRamUserDO, AliyunRamUserVO.class);
        // TODO 插入用户策略
        aliyunRamUserVO.setPolicyList(listPoliciesForUser(aliyunRamUserDO.getRamUserName()));
        // TODO 插入用户AccessKeys
        aliyunRamUserVO.setAccessKeyList(listAccessKeys(aliyunRamUserDO.getRamUserName()));
        if (aliyunRamUserDO.getUserId() > 0) {
            UserDO userDO = userDao.getUserById(aliyunRamUserDO.getUserId());
            UserVO userVO = new UserVO(userDO, true);
            aliyunRamUserVO.setUserVO(userVO);
        }

        return aliyunRamUserVO;
    }

    @Override
    public BusinessWrapper<Boolean> updateRamUsers() {
        List<ListUsersResponse.User> userList = listAllUsers();
        try {
            for (ListUsersResponse.User user : userList) {
                AliyunRamUserDO aliyunRamUserDO = aliyunDao.getRamUserByName(user.getUserName());
                if (aliyunRamUserDO == null) {
                    AliyunRamUserDO ramUserDO = new AliyunRamUserDO(user);
                    // TODO 插入激活的Accesskeys数量
                    ramUserDO.setAccessKeys(listAccessKeys(user.getUserName()).size());
                    ramUserDO.setUserId(queryUser(user.getUserName()));
                    aliyunDao.addRamUser(ramUserDO);
                } else {
                    aliyunRamUserDO.setRamDisplayName(user.getDisplayName());
                    aliyunRamUserDO.setMobilePhone(user.getMobilePhone());
                    aliyunRamUserDO.setEmail(user.getEmail());
                    aliyunRamUserDO.setComments(user.getComments());
                    aliyunRamUserDO.setAccessKeys(listAccessKeys(user.getUserName()).size());
                    aliyunDao.updateRamUser(aliyunRamUserDO);
                }
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<Boolean>(false);
    }

    /**
     * 查询本地用户的userId
     *
     * @param ramUserName
     * @return
     */
    private long queryUser(String ramUserName) {
        String queryUsername = ramUserName;
        if (ramUserName.indexOf("gegejia_", 0) == 0) {
            queryUsername = ramUserName.replace("gegejia_", "");
        }
        UserDO userDO = userDao.getUserByName(queryUsername);
        if (userDO == null) return 0;
        return userDO.getId();
    }

    private IAcsClient acqIAcsClient(String regionId) {
        HashMap<String, String> configMap = acqConifMap();
        String aliyunAccessKey = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_KEY.getItemKey());
        String aliyunAccessSecret = configMap.get(AliyunEcsItemEnum.ALIYUN_ECS_ACCESS_SECRET.getItemKey());
        IClientProfile profile = DefaultProfile.getProfile(regionId, aliyunAccessKey, aliyunAccessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
