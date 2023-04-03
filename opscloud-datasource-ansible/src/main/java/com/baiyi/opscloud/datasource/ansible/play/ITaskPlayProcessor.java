package com.baiyi.opscloud.datasource.ansible.play;

import jakarta.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:23 下午
 * @Version 1.0
 */
public interface ITaskPlayProcessor {

    /**
     * @param message
     * @param session
     */
    void process(String message, Session session);

    String getState();

}
