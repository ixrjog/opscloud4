package com.sdg.cmdb.plugin.chain;

public abstract class TaskCallback<T> {
    /**
     * 回调通知
     * @param notify
     */
    public abstract void doNotify(T notify);
}
