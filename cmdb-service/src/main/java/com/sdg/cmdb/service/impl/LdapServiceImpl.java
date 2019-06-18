package com.sdg.cmdb.service.impl;

import com.google.common.base.Joiner;
import com.sdg.cmdb.dao.cmdb.LdapDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.ldap.LdapGroupDO;
import com.sdg.cmdb.domain.ldap.LdapGroupVO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailLdapGroup;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.LdapService;
import com.sdg.cmdb.util.PasswdUtils;
import com.sdg.cmdb.util.RegexUtils;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.schedule.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.*;

@Service
public class LdapServiceImpl implements LdapService {

    private static final Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");


    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;


    @Value("#{cmdb['ldap.manager.dn']}")
    private String manageDn;

    // LDAP 配置 ou=system
    @Value("#{cmdb['ldap.base.dn']}")
    private String baseDn;

    // LDAP 配置 ou=groups
    @Value("#{cmdb['ldap.group.dn']}")
    private String groupDn;

    // LDAP 配置 ou=users
    @Value("#{cmdb['ldap.user.dn']}")
    private String userDn;

    // LDAP 配置 inetorgperson
    @Value("#{cmdb['ldap.user.object']}")
    private String userObject;

    // LDAP 配置 groupOfUniqueNames
    @Value("#{cmdb['ldap.group.object']}")
    private String groupObject;

    // LDAP 配置 uniqueMember
    @Value("#{cmdb['ldap.group.member']}")
    private String groupMember;

    // LDAP 配置 cn
    @Value("#{cmdb['ldap.user.id']}")
    private String userId;

    @Resource
    private UserDao userDao;

    @Resource
    private AuthService authService;

    @Autowired
    private LdapDao ldapDao;

    @Resource
    private LDAPFactory ldapFactory;

    @Resource
    private SchedulerManager schedulerManager;

    private String getGroupDn() {
        if (StringUtils.isEmpty(baseDn)) {
            return groupDn;
        } else {
            return groupDn + "," + baseDn;
        }
    }

    private String getUserDn() {
        if (StringUtils.isEmpty(baseDn)) {
            return userDn;
        } else {
            return userDn + "," + baseDn;
        }
    }


    @Override
    public boolean checkUserInLdap(String username) {
        try {
            String dn = userId + "=" + username + "," + getUserDn();
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(dn);
            String cn = adapter.getStringAttribute(userId);
            if (username.equalsIgnoreCase(cn)) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * LDAP新建Group
     *
     * @param cn
     * @return
     */
    @Override
    public boolean addLdapGroup(String cn) {
        String dn = userId + "=" + cn + "," + getGroupDn();
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        ocattr.add("top");
        ocattr.add(groupObject);
        // 用户属性
        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put(userId, cn);
        // "uid=admin,ou=system"
        attrs.put(groupMember, manageDn);
        // ldapFactory.getLdapTemplateInstance().bind(dn, null, attrs);

        if (!ldapBind(dn, null, attrs, ldapFactory.getLdapTemplateInstance()))
            return false;
        // 更新LDAP Slave
        List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
        for (LdapTemplate ldapSlave : slaveList)
            ldapBind(dn, null, attrs, ldapSlave);
        return true;
    }

    private boolean ldapBind(String dn, Object obj, Attributes attrs, LdapTemplate ldapTemplate) {
        try {
            ldapTemplate.bind(dn, obj, attrs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户信息（修改密码，邮箱，手机等）
     *
     * @param userVO
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> updateUser(UserVO userVO) {
        if (StringUtils.isEmpty(userVO.getUsername()))
            return new BusinessWrapper<>(ErrorCode.usernameIsNull);
        // check 非本人必须为管理员
        String username = SessionUtils.getUsername();
        if (!username.equals(userVO.getUsername())) {
            if (!authService.isRole(username, RoleDO.roleAdmin))
                return new BusinessWrapper<>(false);
        }
        // 判断密码强度是否符合要求
        if (!StringUtils.isEmpty(userVO.getUserpassword())) {
            if (!RegexUtils.checkPassword(userVO.getUserpassword()))
                return new BusinessWrapper<>(ErrorCode.userpasswordNonConformity);
        }
        return updateUserBase(userVO, false);
    }

    /**
     * @param userVO
     * @param safe   true：不允许修改管理员账户信息
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> updateUserBase(UserVO userVO, boolean safe) {
        // 插入主键
        if (userVO.getId() <= 0) {
            UserDO userDO = userDao.getUserByName(userVO.getUsername());
            if (userDO == null)
                return new BusinessWrapper<>(ErrorCode.usernameIsNull);
            userVO.setId(userDO.getId());
        }
        // 判断管理员
        if (safe) {
            if (authService.isRole(userVO.getUsername(), RoleDO.roleAdmin))
                return new BusinessWrapper<>(ErrorCode.userpasswordNonConformity);
        }
        // 判断LDAP是否存在此用户
        UserDO userDO = getUserByName(userVO.getUsername());
        if (userDO != null) {
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getDisplayName()))
                userDO.setDisplayName(userVO.getDisplayName());
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getMail()))
                userDO.setMail(userVO.getMail());
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getMobile()))
                userDO.setMobile(userVO.getMobile());
            userDao.saveUserInfo(userDO);
            // 更新LDAP Master
            if (!updateUserBase(userVO, ldapFactory.getLdapTemplateInstance()))
                return new BusinessWrapper<>(false);
            // 更新LDAP Slave
            List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
            for (LdapTemplate ldapSlave : slaveList)
                updateUserBase(userVO, ldapSlave);
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(false);
        }

    }

    private boolean updateUserBase(UserVO userVO, LdapTemplate ldapTemplate) {
        try {
            String dn = userId + "=" + userVO.getUsername() + "," + getUserDn();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getDisplayName()))
                ldapTemplate.modifyAttributes(dn, new ModificationItem[]{
                        new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("displayName", userVO.getDisplayName()))
                });
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getMail()))
                ldapTemplate.modifyAttributes(dn, new ModificationItem[]{
                        new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mail", userVO.getMail()))
                });
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getMobile()))
                ldapTemplate.modifyAttributes(dn, new ModificationItem[]{
                        new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mobile", userVO.getMobile()))
                });
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userVO.getUserpassword()))
                ldapTemplate.modifyAttributes(dn, new ModificationItem[]{
                        new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userpassword", userVO.getUserpassword()))
                });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public BusinessWrapper<Boolean> addLdapGroup(String username, String groupname) {
        //UserDO userDO = userDao.getUserByName(username);
        //if (userDO == null) return new BusinessWrapper<>(false);
        if (isGroupMember(username, groupname)) return new BusinessWrapper<>(true);
        if (chgMemberToGroup(username, groupname, DirContext.ADD_ATTRIBUTE)) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public boolean isGroupMember(String username, String groupName) {
        String dn = userId + "=" + username + "," + getUserDn();
        List<String> groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(getGroupDn())
                        .where(groupMember).is(dn).and(userId).like(groupName),
                (Attributes attributes) -> attributes.get(userId).get(0).toString()
        );
        if (groups.size() == 0)
            return false;
        return true;
    }


    @Override
    public BusinessWrapper<Boolean> delLdapGroup(String username, String groupname) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) return new BusinessWrapper<>(false);
        //if (!isGroupMember(username, groupname)) return new BusinessWrapper<>(true);
        if (chgMemberToGroup(username, groupname, DirContext.REMOVE_ATTRIBUTE)) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(false);
        }
    }


    @Override
    public List<String> searchLdapGroup() {
        List<String> groups = Collections.EMPTY_LIST;
        try {
            groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(getGroupDn())
                            .where("objectClass").is(groupObject).and(userId).like("*"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException {
                            return attributes.get(userId).get(0).toString();
                        }
                    }
            );
        } catch (Exception e) {
            logger.warn("search ldap group error={}", e.getMessage(), e);
        }
        return groups;
    }


    /**
     * 查询所有的bamboo用户组
     *
     * @return
     */
    @Override
    public List<String> searchBambooGroup() {
        String groupFilter = "ci_";
        List<String> groups = Collections.EMPTY_LIST;
        try {
            groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(getGroupDn())
                            .where("objectClass").is(groupObject).and(userId).like(groupFilter + "*"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException {
                            return attributes.get(userId).get(0).toString();
                        }
                    }
            );
        } catch (Exception e) {
            logger.warn("search bamboo group error={}", e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public UserDO getUserByName(String username) {
        try {
            String dn = userId + "=" + username + "," + getUserDn();
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(dn);
            String mail = adapter.getStringAttribute("mail");
            String displayName = adapter.getStringAttribute("displayname");
            String mobile = adapter.getStringAttribute("mobile");
            UserDO userDO = new UserDO(mail, displayName, username, UUID.randomUUID().toString());
            userDO.setMobile(mobile);
            userDO.setInvalid(UserDO.Invalid.invalid.getCode());
            //如果db有内容,则置入pwd
            UserDO dbUserDO = userDao.getUserByName(username);
            if (dbUserDO != null) {
                userDO.setPwd(dbUserDO.getPwd());
                userDO.setRsaKey(dbUserDO.getRsaKey());
                if (!org.apache.commons.lang3.StringUtils.isEmpty(dbUserDO.getMobile()))
                    userDO.setMobile(dbUserDO.getMobile());
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(userDO.getPwd())) {
                userDO.setPwd(PasswdUtils.getRandomPasswd(0));
            }
            return userDO;
        } catch (Exception e) {
            if (username.equals("admin")) {
                UserDO dbUserDO = userDao.getUserByName(username);
                dbUserDO.setToken(UUID.randomUUID().toString());
                return dbUserDO;
            }
            return null;
        }
    }


    @Override
    public boolean delUser(String username) {
        try {
            //删除ldap
            LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
            coreLogger.warn("del user for:" + username);
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter(userId, username));
            List list = ldapTemplate.search("", filter.encode(), (Attributes attrs) -> {
                return attrs.get(userId).get();
            });
            if (!list.isEmpty()) {
                String dn = userId + "=" + username + "," + getUserDn();
                // LDAP Master 解除绑定
                ldapUnbind(dn, ldapTemplate);
                List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
                // LDAP Slave 解除绑定
                for (LdapTemplate ldapSlave : slaveList)
                    ldapUnbind(dn, ldapSlave);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean ldapUnbind(String dn, LdapTemplate ldapTemplate) {
        try {
            ldapTemplate.unbind(dn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public BusinessWrapper<Boolean> removeMember(String username) {
        try {
            schedulerManager.registerJob(() -> {
                List<String> groups = searchLdapGroup(username);
                for (String groupName : groups)
                    removeMember2Group(username, groupName);
            });
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
    }


    @Override
    public boolean removeMember2Group(String username, String groupName) {
        String groupDn = userId + "=" + groupName + "," + getGroupDn();
        String userDn = userId + "=" + username + "," + getUserDn();

        if (!removeMember2Group(userDn, groupDn, ldapFactory.getLdapTemplateInstance()))
            return false;
        // 更新LDAP Slave
        List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
        for (LdapTemplate ldapSlave : slaveList)
            removeMember2Group(userDn, groupDn, ldapSlave);
        return true;
    }

    private boolean removeMember2Group(String userDn, String groupDn, LdapTemplate ldapTemplate) {
        try {
            ldapTemplate.modifyAttributes(groupDn, new ModificationItem[]{
                    new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(groupMember, userDn))
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public BusinessWrapper<Boolean> delUserToGroup(String groupName, String username) {
        return new BusinessWrapper<Boolean>(removeMember2Group(username, groupName));
    }

    @Override
    public List<String> searchLdapGroup(String username) {
        String dn = userId + "=" + username + "," + getUserDn();
        List<String> groups = Collections.EMPTY_LIST;
        try {
            groups = ldapFactory.getLdapTemplateInstance().search(LdapQueryBuilder.query().base(getGroupDn())
                            .where(groupMember).is(dn).and(userId).like("*"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException {
                            return attributes.get(userId).get(0).toString();
                        }
                    }
            );
        } catch (Exception e) {
            logger.warn("username={} search ldap group error={}", username, e.getMessage(), e);
        }
        return groups;
    }

    @Override
    public List<LdapGroup> searchUserLdapGroup(String username) {
        // TODO 查询所有的LDAP组
        List<String> ldapGroups = searchLdapGroup();
        // TODO 查询用户bind的组
        List<String> userGroups = searchLdapGroup(username);
        HashMap<String, Boolean> map = new HashMap<>();
        for (String groupName : ldapGroups) {
            map.put(groupName, false);
        }
        for (String userGroup : userGroups) {
            map.put(userGroup, true);
        }
        List<LdapGroup> result = new ArrayList<>();
        for (String key : map.keySet()) {
            LdapGroupDO lgDO = ldapDao.getLdapGroupByCn(key);
            result.add(new LdapGroup(key, map.get(key), lgDO.getContent()));
        }
        return result;
    }


    @Override
    public List<UserVO> searchLdapGroupUsers(String groupname) {
        // "cn=Administrators,ou=groups,ou=system")
        String groupDN = "cn=" + groupname + "," + getGroupDn();
        // cn=admin-jenkins,ou=users,ou=system
        try {
            DirContextAdapter adapter = (DirContextAdapter) ldapFactory.getLdapTemplateInstance().lookup(groupDN);
            //"uniqueMember"
            String[] members = adapter.getStringAttributes(groupMember);
            List<UserVO> users = new ArrayList<>();
            for (String member : members) {
                String[] m = member.split("=|,");
                if (m.length > 2 && !m[1].equals("admin")) {
                    UserDO userDO = userDao.getUserByName(m[1]);
                    if (userDO != null)
                        users.add(new UserVO(userDO, true));
                }
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public List<TodoDetailLdapGroup> getUserWorkflowLdapGroup(String username) {
        List<LdapGroupDO> ldapGroups = ldapDao.getLdapGroupByWorkflow();
        // TODO 查询用户bind的组
        List<String> userGroups = searchLdapGroup(username);
        HashMap<String, Boolean> map = new HashMap<>();
        for (String cn : userGroups) {
            map.put(cn, true);
        }
        List<TodoDetailLdapGroup> groups = new ArrayList<>();
        for (LdapGroupDO ldapGroupDO : ldapGroups) {
            TodoDetailLdapGroup todoDetailLdapGroup = new TodoDetailLdapGroup(ldapGroupDO, map.containsKey(ldapGroupDO.getCn()));
            groups.add(todoDetailLdapGroup);
        }
        return groups;
    }


    @Override
    public boolean checkUserInLdapGroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        List<String> groups = searchLdapGroup(userDO.getUsername());
        for (String groupName : groups)
            if (serverGroupDO.getName().equals(groupName)) return true;
        return false;
    }


    @Override
    public boolean addMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        // 判断是否已经添加了用户
        if (checkUserInLdapGroup(userDO, serverGroupDO)) return true;
        return chgMemberToGroup(userDO, serverGroupDO, DirContext.ADD_ATTRIBUTE);
    }

    @Override
    public boolean addMemberToGroup(UserDO userDO, String groupName) {
        if (checkUserInLdapGroup(userDO, groupName)) return true;
        return chgMemberToGroup(userDO.getUsername(), groupName, DirContext.ADD_ATTRIBUTE);
    }

    @Override
    public BusinessWrapper<Boolean> addUserToGroup(String groupName, String username) {
        UserDO userDO = userDao.getUserByName(username);
        if (userDO == null) return new BusinessWrapper<Boolean>(false);
        return new BusinessWrapper<Boolean>(addMemberToGroup(userDO, groupName));
    }


    @Override
    public boolean delMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO) {
        // 判断是否已经添加了用户
        if (!checkUserInLdapGroup(userDO, serverGroupDO)) return true;
        return chgMemberToGroup(userDO.getUsername(), serverGroupDO.getName(), DirContext.REMOVE_ATTRIBUTE);
    }

    @Override
    public boolean delMemberToGroup(UserDO userDO, String groupName) {
        if (!checkUserInLdapGroup(userDO, groupName)) return true;
        return chgMemberToGroup(userDO.getUsername(), groupName, DirContext.REMOVE_ATTRIBUTE);
    }

    public boolean checkUserInLdapGroup(UserDO userDO, String groupName) {
        List<String> groups = searchLdapGroup(userDO.getUsername());
        for (String name : groups) {
            if (name.equals(groupName)) return true;
        }
        return false;
    }


    private boolean chgMemberToGroup(UserDO userDO, ServerGroupDO serverGroupDO, int type) {
        return chgMemberToGroup(userDO.getUsername(), serverGroupDO.getName(), type);
    }

    private boolean chgMemberToGroup(String username, String groupName, int type) {
        String groupDn = userId + "=" + groupName + "," + getGroupDn();
        String userDn = userId + "=" + username + "," + getUserDn();
        LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
        // 更新LDAP Master
        if (!chgMemberToGroup(groupDn, userDn, type, ldapTemplate))
            return false;
        // 更新LDAP Slave
        List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
        for (LdapTemplate ldapSlave : slaveList)
            chgMemberToGroup(groupDn, userDn, type, ldapSlave);
        return true;
    }

    private boolean chgMemberToGroup(String groupDn, String userDn, int type, LdapTemplate ldapTemplate) {
        try {
            ldapTemplate.modifyAttributes(groupDn, new ModificationItem[]{
                    new ModificationItem(type, new BasicAttribute(groupMember, userDn))
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    /**
     * LDAP 删除组内用户的其它写法
     *
     * @param username
     * @param groupName
     * @return
     */
    public boolean removeUserToGroup(String username, String groupName) {
        try {
            String groupDn = userId + "=" + groupName + "," + getGroupDn();
            String userDn = userId + "=" + username + "," + getUserDn();
            LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
            DirContextOperations ctxGroup = ldapTemplate.lookupContext(groupDn);
            DirContextOperations ctxUser = ldapTemplate.lookupContext(userDn);
            String x = ctxUser.getStringAttribute("cn");
            ctxGroup.removeAttributeValue("uniquemember", userDn);
            ldapTemplate.modifyAttributes(ctxGroup);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public BusinessWrapper<Boolean> createLdapGroup(String groupName, String content, int groupType) {
        try {
            if (!addLdapGroup(groupName))
                return new BusinessWrapper<Boolean>(false);
            LdapGroupDO ldapGroupDO = new LdapGroupDO(groupName, content, groupType);
            ldapDao.addLdapGroup(ldapGroupDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delLdapGroup(long id) {
        LdapGroupDO ldapGroupDO = ldapDao.getLdapGroup(id);
        if (ldapGroupDO == null)
            return new BusinessWrapper<Boolean>(false);
        try {
            //删除ldap
            LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
            coreLogger.warn("del ldap group cn:" + ldapGroupDO.getCn());
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter(userId, ldapGroupDO.getCn()));
            List list = ldapTemplate.search("", filter.encode(), (Attributes attrs) -> {
                return attrs.get(userId).get();
            });
            if (!list.isEmpty()) {
                String dn = userId + "=" + ldapGroupDO.getCn() + "," + getGroupDn();
                // LDAP Master 解除绑定
                ldapUnbind(dn, ldapTemplate);
                List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
                // LDAP Slave 解除绑定
                for (LdapTemplate ldapSlave : slaveList)
                    ldapUnbind(dn, ldapSlave);
            }
            ldapDao.delLdapGroup(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }


    }

    @Override
    public TableVO<List<LdapGroupVO>> getLdapGroupPage(String cn, int groupType, int page, int length) {
        long size = ldapDao.getLdapGroupSize(cn);
        List<LdapGroupDO> ldapGroups = ldapDao.getLdapGroupPage(cn, groupType, page * length, length);
        List<LdapGroupVO> voList = new ArrayList<>();
        for (LdapGroupDO ldapGroupDO : ldapGroups) {
            List<UserVO> ldapUsers = searchLdapGroupUsers(ldapGroupDO.getCn());
            LdapGroupVO ldapGroupVO = new LdapGroupVO(ldapGroupDO, ldapUsers);
            voList.add(ldapGroupVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public LdapGroupVO getLdapGroup(String cn) {
        LdapGroupDO ldapGroupDO = ldapDao.getLdapGroupByCn(cn);
        List<UserVO> ldapUsers = searchLdapGroupUsers(ldapGroupDO.getCn());
        LdapGroupVO ldapGroupVO = new LdapGroupVO(ldapGroupDO, ldapUsers);
        return ldapGroupVO;
    }

    @Override
    public boolean addUser(UserVO userVO) {
        UserDO userDO = getUserByName(userVO.getUsername());
        if (userDO == null) {
            try {
                String dn = "cn=" + userVO.getUsername() + "," + getUserDn();
                // 基类设置
                BasicAttribute ocattr = new BasicAttribute("objectClass");
                ocattr.add("top");
                ocattr.add("person");
                //ocattr.add("uidObject");
                ocattr.add(userObject);
                ocattr.add("organizationalPerson");
                // 用户属性
                Attributes attrs = new BasicAttributes();
                attrs.put(ocattr);
                attrs.put(userId, userVO.getUsername());
                attrs.put("sn", userVO.getUsername());
                attrs.put("displayName", userVO.getDisplayName());
                attrs.put("mail", userVO.getMail());
                attrs.put("userPassword", userVO.getUserpassword());
                if (org.apache.commons.lang3.StringUtils.isEmpty(userVO.getMobile())) {
                    attrs.put("mobile", "0");
                } else {
                    attrs.put("mobile", userVO.getMobile());
                }

                if (!ldapBind(dn, null, attrs, ldapFactory.getLdapTemplateInstance()))
                    return false;
                // 更新LDAP Slave
                List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
                for (LdapTemplate ldapSlave : slaveList)
                    ldapBind(dn, null, attrs, ldapSlave);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public BusinessWrapper<Boolean> unbindUser(String username) {
        LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
        //HashMap<String, String> configMap = acqConfigMap();
        //String userDN = configMap.get(LdapItemEnum.LDAP_USER_DN.getItemKey());
        String dn = "cn=" + username + "," + getUserDn();
        // LDAP Master 解除绑定
        ldapUnbind(dn, ldapTemplate);
        List<LdapTemplate> slaveList = ldapFactory.getLdapTemplateSlaveInstance();
        // LDAP Slave 解除绑定
        for (LdapTemplate ldapSlave : slaveList)
            ldapUnbind(dn, ldapSlave);
        //删除ldap
        try {
            // 解除绑定的组
            removeMember(username);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(false);
        }
    }

    @Override
    public String getUserDN(String username) {
        return Joiner.on(",").join("cn=" + username, getUserDn());
    }


}
