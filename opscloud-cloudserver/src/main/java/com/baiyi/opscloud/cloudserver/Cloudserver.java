package com.baiyi.opscloud.cloudserver;

/**
 * @Author baiyi
 * @Date 2020/1/10 4:21 下午
 * @Version 1.0
 */
public interface Cloudserver {

    /**
     * 同步
     * @return
     */
    Boolean sync();

    /**
     * 同步并推送主机名
     * @param pushName
     * @return
     */
    Boolean sync(boolean pushName);

    String getKey();
}
