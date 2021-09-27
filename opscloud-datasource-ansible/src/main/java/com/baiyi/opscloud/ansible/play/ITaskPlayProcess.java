package com.baiyi.opscloud.ansible.play;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:23 下午
 * @Version 1.0
 */
public interface ITaskPlayProcess {

    /**
     * @param message
     * @param session
     */
    void process(String message, Session session);

    String getState();
}
