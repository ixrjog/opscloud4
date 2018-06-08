package com.sdg.cmdb.extend.plugin;

import com.sdg.cmdb.extend.Chain;
import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.Plugin;

/**
 * Created by liangjian on 2017/5/25.
 */


public interface ZabbixPlugin extends Plugin, Chain {
    /**
     * 登录校验
     * @param invocation  登录上下文信息
     * @return
     */
    boolean loginCheck(Invocation invocation);
}