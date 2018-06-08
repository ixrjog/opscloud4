package com.sdg.cmdb.login.simple;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.Invoker;
import com.sdg.cmdb.extend.Result;
import com.sdg.cmdb.extend.plugin.LoginPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2017/5/15.
 */
@Service
public class SimpleLoginPluginImpl implements LoginPlugin {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLoginPluginImpl.class);

    @Value("#{cmdb['admin.passwd']}")
    private String adminPasswd;



    @Override
    public Result doInvoke(Invoker<?> invoker, Invocation invocation) {
        //TODO 简单登录校验逻辑缺失，需要补齐
        return null;
    }

    @Override
    public boolean loginCheck(Invocation invocation) {
        logger.info("login check content={}", JSON.toJSONString(invocation));

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

        if (username.equals("admin") && password.equals(adminPasswd)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getPluginImpl() {
        return "simple";
    }
}
