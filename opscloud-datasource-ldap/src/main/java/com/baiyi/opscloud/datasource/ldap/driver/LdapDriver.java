package com.baiyi.opscloud.datasource.ldap.driver;


import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.datasource.ldap.entity.LdapGroup;
import com.baiyi.opscloud.datasource.ldap.entity.LdapPerson;
import com.baiyi.opscloud.datasource.ldap.mapper.GroupAttributesMapper;
import com.baiyi.opscloud.datasource.ldap.mapper.PersonAttributesMapper;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.datasource.ldap.factory.LdapFactory;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import javax.naming.directory.*;
import java.util.Collections;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Ldap 通用处理
 */
@Slf4j
@Component
public class LdapDriver {

    public static final String GROUP_MEMBER = "groupMember";

    public interface SEARCH_KEY {
        String OBJECTCLASS = "objectclass";
    }

    private LdapTemplate buildLdapTemplate(LdapConfig.Ldap config) {
        return LdapFactory.buildLdapTemplate(config);
    }


    /**
     * 查询所有Person
     *
     * @return
     */
    public List<LdapPerson.Person> queryPersonList(LdapConfig.Ldap ldapConfig) {
        return buildLdapTemplate(ldapConfig)
                .search(query().where(SEARCH_KEY.OBJECTCLASS).is(ldapConfig.getUser().getObjectClass()), new PersonAttributesMapper());
    }

    /**
     * 查询所有Person username
     *
     * @return
     */
    public List<String> queryPersonNameList(LdapConfig.Ldap ldapConfig) {
        return buildLdapTemplate(ldapConfig).search(
                query().where(SEARCH_KEY.OBJECTCLASS).is(ldapConfig.getUser().getObjectClass()), (AttributesMapper<String>) attrs
                        -> (String) attrs.get(ldapConfig.getUser().getId()).get());
    }

    /**
     * 通过dn查询Person
     *
     * @param dn
     * @return
     */
    public LdapPerson.Person getPersonWithDN(LdapConfig.Ldap ldapConfig, String dn) {
        return buildLdapTemplate(ldapConfig).lookup(dn, new PersonAttributesMapper());
    }

    /**
     * 通过dn查询Group
     *
     * @param dn
     * @return
     */
    public LdapGroup.Group getGroupWithDN(LdapConfig.Ldap ldapConfig, String dn) {
        return buildLdapTemplate(ldapConfig).lookup(dn, new GroupAttributesMapper());
    }

    /**
     * 校验登录
     *
     * @param credential
     * @return
     */
    public boolean verifyLogin(LdapConfig.Ldap ldapConfig, Authorization.Credential credential) {
        if (credential.isEmpty()) return false;
        String username = credential.getUsername();
        String password = credential.getPassword();
        log.info("Verify login content username={}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter(ldapConfig.getUser().getId(), username));
        try {
            return buildLdapTemplate(ldapConfig).authenticate(ldapConfig.getUser().getDn(), filter.toString(), password);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 解除绑定
     *
     * @param dn
     */
    public void unbind(LdapConfig.Ldap ldapConfig, String dn) {
        buildLdapTemplate(ldapConfig).unbind(dn);
    }

    private void bind(LdapConfig.Ldap ldapConfig, String dn, Object obj, Attributes attrs) {
        buildLdapTemplate(ldapConfig).bind(dn, obj, attrs);
    }

    /**
     * 绑定用户
     *
     * @param person
     * @return
     */
    public void bindPerson(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        final String userId = ldapConfig.getUser().getId();
        final String userBaseDN = ldapConfig.getUser().getDn();
        final String userObjectClass = ldapConfig.getUser().getObjectClass();

        try {
            final String rdn = toUserRDN(ldapConfig, person);
            final String dn = Joiner.on(",").skipNulls().join(rdn, userBaseDN);
            // 基类设置
            BasicAttribute ocattr = new BasicAttribute("objectClass");
            ocattr.add("top");
            ocattr.add("person");
            ocattr.add("organizationalPerson");
            if (!userObjectClass.equalsIgnoreCase("person") && !userObjectClass.equalsIgnoreCase("organizationalPerson"))
                ocattr.add(userObjectClass);
            // 用户属性
            Attributes attrs = new BasicAttributes();
            attrs.put(ocattr);
            attrs.put(userId, person.getUsername()); // cn={username}
            attrs.put("sn", person.getUsername());
            attrs.put("displayName", person.getDisplayName());
            attrs.put("mail", person.getEmail());
            attrs.put("userPassword", person.getUserPassword());
            attrs.put("mobile", (StringUtils.isEmpty(person.getMobile()) ? "0" : person.getMobile()));
            bind(ldapConfig, dn, null, attrs);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 绑定用户组
     *
     * @param group
     * @return
     */
    public void bindGroup(LdapConfig.Ldap ldapConfig, LdapGroup.Group group) {
        final String groupId = ldapConfig.getGroup().getId();
        final String groupBaseDN = ldapConfig.getGroup().getDn();
        final String groupObjectClass = ldapConfig.getGroup().getObjectClass();

        final String rdn = toGroupRDN(ldapConfig, group);
        final String dn = Joiner.on(",").skipNulls().join(rdn, groupBaseDN);
        // 基类设置
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        ocattr.add("top");
        ocattr.add(groupObjectClass);
        // 用户属性
        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put(groupId, group.getGroupName()); // cn={groupName}
        // 添加一个空成员
        // attrs.put(GROUP_MEMBER, "");
        try {
            bind(ldapConfig, dn, null, attrs);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    private String toUserRDN(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        return Joiner.on("=").join(ldapConfig.getUser().getId(), person.getUsername());
    }

    private String toGroupRDN(LdapConfig.Ldap ldapConfig, LdapGroup.Group group) {
        return Joiner.on("=").join(ldapConfig.getUser().getId(), group.getGroupName());
    }

    private String toUserDN(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        String rdn = toUserRDN(ldapConfig, person);
        return Joiner.on(",").join(rdn, ldapConfig.getUser().getDn());
    }

    public void updatePerson(LdapConfig.Ldap ldapConfig, LdapPerson.Person person) {
        String dn = toUserDN(ldapConfig, person);
        LdapPerson.Person checkPerson = getPersonWithDN(ldapConfig, dn);
        if (checkPerson == null) return;
        try {
            if (!StringUtils.isEmpty(person.getDisplayName()) && !person.getDisplayName().equals(checkPerson.getDisplayName()))
                modifyAttributes(ldapConfig, dn, "displayName", person.getDisplayName());
            if (!StringUtils.isEmpty(person.getEmail()) && !person.getEmail().equals(checkPerson.getEmail()))
                modifyAttributes(ldapConfig, dn, "mail", person.getEmail());
            if (!StringUtils.isEmpty(person.getMobile()) && !person.getMobile().equals(checkPerson.getMobile()))
                modifyAttributes(ldapConfig, dn, "mobile", person.getMobile());
            if (!StringUtils.isEmpty(person.getUserPassword()))
                modifyAttributes(ldapConfig, dn, "userpassword", person.getUserPassword());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 查询所有Group
     *
     * @return
     */
    public List<LdapGroup.Group> queryGroupList(LdapConfig.Ldap ldapConfig) {
        return buildLdapTemplate(ldapConfig)
                .search(query().where(SEARCH_KEY.OBJECTCLASS).is(ldapConfig.getGroup().getObjectClass()), new GroupAttributesMapper());
    }

    private String toGroupRDN(LdapConfig.Ldap ldapConfig, String groupName) {
        return Joiner.on("=").join(ldapConfig.getGroup().getId(), groupName);
    }

    private String toGroupDN(LdapConfig.Ldap ldapConfig, String groupName) {
        String rdn = toGroupRDN(ldapConfig, groupName);
        return Joiner.on(",").join(rdn, ldapConfig.getGroup().getDn());
    }

    public List<String> queryGroupMember(LdapConfig.Ldap ldapConfig, String groupName) {
        try {
            DirContextAdapter adapter = (DirContextAdapter) buildLdapTemplate(ldapConfig).lookup(toGroupDN(ldapConfig, groupName));
            String[] members = adapter.getStringAttributes(ldapConfig.getGroup().getMemberAttribute());
            List<String> usernameList = Lists.newArrayList();
            for (String member : members) {
                String[] m = member.split("[=,]");
                if (m.length > 2 && !m[1].equals("admin"))
                    usernameList.add(m[1]);
            }
            return usernameList;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public void removeGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username) {
        modificationGroupMember(ldapConfig, groupName, username, DirContext.REMOVE_ATTRIBUTE);
    }

    public void addGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username) {
        modificationGroupMember(ldapConfig, groupName, username, DirContext.ADD_ATTRIBUTE);
    }

    private void modificationGroupMember(LdapConfig.Ldap ldapConfig, String groupName, String username, int modificationType) {
        String userDn = toUserDN(ldapConfig, LdapPerson.Person.builder()
                .username(username)
                .build());

        String userFullDn = Joiner.on(",").skipNulls().join(userDn, ldapConfig.getBase());
        try {
            buildLdapTemplate(ldapConfig).modifyAttributes(toGroupDN(ldapConfig, groupName), new ModificationItem[]{
                    new ModificationItem(modificationType, new BasicAttribute(ldapConfig.getGroup().getMemberAttribute(), userFullDn))
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modifyAttributes(LdapConfig.Ldap ldapConfig, String dn, String attrId, String value) {
        buildLdapTemplate(ldapConfig).modifyAttributes(dn, new ModificationItem[]{
                new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attrId, value))
        });
    }

    public boolean hasPersonInLdap(LdapConfig.Ldap ldapConfig, String username) {
        String userDn = toUserDN(ldapConfig, LdapPerson.Person.builder()
                .username(username)
                .build());
        try {
            DirContextAdapter adapter = (DirContextAdapter) buildLdapTemplate(ldapConfig).lookup(userDn);
            String cn = adapter.getStringAttribute(ldapConfig.getUser().getId());
            if (username.equalsIgnoreCase(cn)) return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public List<String> searchLdapGroup(LdapConfig.Ldap ldapConfig, String username) {
        List<String> groupList = Lists.newArrayList();
        try {
            String groupBaseDN = ldapConfig.getGroup().getDn();
            String groupMember = ldapConfig.getGroup().getMemberAttribute();
            String userId = ldapConfig.getUser().getId();
            String userDn = toUserDN(ldapConfig, LdapPerson.Person.builder()
                    .username(username)
                    .build());
            String userFullDn = Joiner.on(",").skipNulls().join(userDn, ldapConfig.getBase());
            groupList = buildLdapTemplate(ldapConfig).search(LdapQueryBuilder.query().base(groupBaseDN)
                            .where(groupMember).is(userFullDn).and(userId).like("*"),
                    (AttributesMapper<String>) attributes -> attributes.get(userId).get(0).toString()
            );
        } catch (Exception e) {
            log.warn("Search ldap group error: username={}, error={}", username, e.getMessage());
        }
        return groupList;
    }

}
