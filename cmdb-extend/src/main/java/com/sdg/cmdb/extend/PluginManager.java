package com.sdg.cmdb.extend;

/**
 * Created by zxxiao on 2017/5/15.
 */
public interface PluginManager {

    /**
     * 执行
     * @param invocation
     * @return
     */
    Result doInvoke(Invocation invocation);
}
