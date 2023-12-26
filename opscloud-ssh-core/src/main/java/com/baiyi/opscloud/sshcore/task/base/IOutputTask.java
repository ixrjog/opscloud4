package com.baiyi.opscloud.sshcore.task.base;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/7/7 12:54 下午
 * @Version 1.0
 */
public interface IOutputTask extends Runnable {

    /**
     * write output
     *
     * @param buf
     * @param off
     * @param len
     */
    void write(char[] buf, int off, int len) throws IOException;

}