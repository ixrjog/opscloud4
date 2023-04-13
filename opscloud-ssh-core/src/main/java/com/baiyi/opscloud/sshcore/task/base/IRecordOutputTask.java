package com.baiyi.opscloud.sshcore.task.base;

import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2023/4/12 23:42
 * @Version 1.0
 */
public interface IRecordOutputTask extends IOutputTask {

    /**
     * 记录
     *
     * @param buf
     * @param off
     * @param len
     * @throws IOException
     */
    void record(char[] buf, int off, int len) throws IOException;

    default void record(char[] buf) throws IOException {
        record(buf, 0, buf.length);
    }

    /**
     * 写并记录
     *
     * @param buf
     * @param off
     * @param len
     * @throws IOException
     */
    default void writeAndRecord(char[] buf, int off, int len) throws IOException {
        write(buf, off, len);
        record(buf, off, len);
    }

}