package com.sdg.cmdb.extend;

/**
 * Created by zxxiao on 2017/5/15.
 */
public interface Invoker<T> {

    /**
     * 获取invoke的类型
     * @return
     */
    Class<T> getInterface();

    /**
     * 执行
     * @param invocation
     * @return
     */
    Result invoke(Invocation invocation);
}
