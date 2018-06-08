package com.sdg.cmdb.extend;

/**
 * 链路
 * Created by zxxiao on 2017/5/15.
 */
public interface Chain {

    /**
     * 执行链路的一环
     * @param invoker
     * @param invocation
     */
    Result doInvoke(Invoker<?> invoker, Invocation invocation);
}
