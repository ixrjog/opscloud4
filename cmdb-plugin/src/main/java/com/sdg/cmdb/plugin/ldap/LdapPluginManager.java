package com.sdg.cmdb.plugin.ldap;

import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.PluginManager;
import com.sdg.cmdb.extend.Result;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Created by zxxiao on 2017/6/23.
 */
@Service
public class LdapPluginManager implements PluginManager, InitializingBean {

    @Override
    public Result doInvoke(Invocation invocation) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
