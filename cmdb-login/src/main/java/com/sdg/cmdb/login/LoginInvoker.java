package com.sdg.cmdb.login;

import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.InvokeResult;
import com.sdg.cmdb.extend.Invoker;
import com.sdg.cmdb.extend.Result;
import com.sdg.cmdb.extend.plugin.LoginPlugin;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 2017/5/15.
 */
public class LoginInvoker implements Invoker<LoginPlugin> {

    /**
     * 执行列表
     */
    private List<LoginPlugin> pluginList = new ArrayList<>();

    public LoginInvoker(List<LoginPlugin> pluginList) {
        Assert.notEmpty(pluginList);
        this.pluginList = pluginList;
    }

    @Override
    public Class getInterface() {
        return LoginPlugin.class;
    }

    @Override
    public Result invoke(Invocation invocation) {
        for(LoginPlugin loginPlugin : pluginList) {
            InvokeResult result = (InvokeResult) loginPlugin.doInvoke(this, invocation);
            if (result !=null && result.isSuccess()) {
                return result;
            }
        }
        InvokeResult result = new InvokeResult<>();
        result.setSuccess(false);
        result.setMsg("登录校验失败！");
        return result;
    }
}
