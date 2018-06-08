package com.sdg.cmdb.extend.plugin;

import com.sdg.cmdb.extend.Chain;
import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.Plugin;

import java.util.Map;

/**
 * 鉴权
 * Created by zxxiao on 2017/5/15.
 */
public interface LoginPlugin extends Plugin, Chain {
    /**
     * 登录校验
     * @param invocation  登录上下文信息
     * @return
     */
    boolean loginCheck(Invocation invocation);
}
