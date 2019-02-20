package com.sdg.cmdb.login.ldap;


import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.InvokeResult;
import com.sdg.cmdb.extend.Invoker;
import com.sdg.cmdb.extend.Result;
import com.sdg.cmdb.extend.plugin.LoginPlugin;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2017/5/15.
 */
@Service
public class LdapLoginPluginImpl implements LoginPlugin {
    private static final Logger logger = LoggerFactory.getLogger(LdapLoginPluginImpl.class);


    // LDAP 配置 cn
    @Value("#{cmdb['ldap.user.id']}")
    private String userId;

    // LDAP 配置 ou=system
    @Value("#{cmdb['ldap.base.dn']}")
    private String baseDn;

    // LDAP 配置 ou=users
    @Value("#{cmdb['ldap.user.dn']}")
    private String userDn;

    @Resource
    private LDAPFactory ldapFactory;

    @Override
    public boolean loginCheck(Invocation invocation) {
        if (StringUtils.isEmpty(invocation)) return false;
        //logger.info("login check content = {}", JSON.toJSONString(invocation));

        Object usernameObj = invocation.getParameterForString("username");
        Object passwordObj = invocation.getParameterForString("password");

        String username, password;
        if (usernameObj == null) {
            username = "";
        } else {
            username = usernameObj.toString();
        }
        if (passwordObj == null) {
            password = "";
        } else {
            password = passwordObj.toString();
        }
        logger.info("login check content username {}", username);

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter(userId, username));
        // LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstanceByType(LdapDO.LdapTypeEnum.cmdb.getDesc());
        LdapTemplate ldapTemplate = ldapFactory.getLdapTemplateInstance();
        //"ou=users1,ou=system"
        try {
            // TODO 过滤条件 filter.toString()
            // TODO 登录密码 password
            String dn = userDn;
            if (!StringUtils.isEmpty(baseDn))
                dn += "," + baseDn;
            boolean authResult = ldapTemplate.authenticate(dn, filter.toString(), password);
            return authResult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
        //boolean authResult = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.toString(), password);
        // ldapTemplate.getContextSource().getContext(username,password);
    }

    @Override
    public Result doInvoke(Invoker<?> invoker, Invocation invocation) {
        InvokeResult result = new InvokeResult();
        if (loginCheck(invocation)) {
            result.setSuccess(true);
            result.setResult(true);
        } else {
            result.setSuccess(false);
            result.setMsg("");
        }
        return result;
    }

    @Override
    public String getPluginImpl() {
        return "ldap";
    }
}
