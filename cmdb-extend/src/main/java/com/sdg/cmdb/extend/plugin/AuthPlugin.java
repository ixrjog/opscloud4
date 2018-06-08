package com.sdg.cmdb.extend.plugin;

import com.sdg.cmdb.extend.Chain;
import com.sdg.cmdb.extend.Invocation;
import com.sdg.cmdb.extend.Plugin;

/**
 * Created by zxxiao on 2017/5/15.
 */
public interface AuthPlugin extends Plugin, Chain {

    /**
     * 鉴权
     * @param invocation   鉴权上下文
     * @return
     */
    boolean authCheck(Invocation invocation);
}
