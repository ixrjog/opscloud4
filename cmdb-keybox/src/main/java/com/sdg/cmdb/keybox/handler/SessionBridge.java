package com.sdg.cmdb.keybox.handler;

/**
 * Created by zxxiao on 2016/12/1.
 */
public abstract class SessionBridge {

    /**
     * 桥接处理器
     * @param value
     */
    public abstract void process(String value);
}
