package com.sdg.cmdb.login;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.extend.*;
import com.sdg.cmdb.extend.plugin.LoginPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 2017/5/15.
 */
@Service
public class LoginPluginManager implements PluginManager, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(LoginPluginManager.class);

    @Resource
    private ApplicationContext applicationContext;

    private Invoker<LoginPlugin> invoker;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, LoginPlugin> loginPluginMap = applicationContext.getBeansOfType(LoginPlugin.class);
        initPluginChain(loginPluginMap);
    }

    /**
     * 组装链路
     * @param loginPluginMap
     */
    private void initPluginChain(Map<String, LoginPlugin> loginPluginMap) {
        if (loginPluginMap == null) {
            logger.error("login plugin chain is null!");
            return;
        }
        logger.info("init login plugin={}", JSON.toJSONString(loginPluginMap.keySet()));

        //当前情况下，默认ldap -> simple，后续此处交由统一配置管理进行自定义控制
        List<LoginPlugin> pluginList = new ArrayList<>();
        for(String pluginName : Arrays.asList("ldap", "simple")) {
            for(LoginPlugin loginPlugin : loginPluginMap.values()) {
                if (loginPlugin.getPluginImpl().equals(pluginName)) {
                    pluginList.add(loginPlugin);
                }
            }
        }

        invoker = new LoginInvoker(pluginList);
    }

    @Override
    public Result doInvoke(Invocation invocation) {
        return invoker.invoke(invocation);
    }
}
