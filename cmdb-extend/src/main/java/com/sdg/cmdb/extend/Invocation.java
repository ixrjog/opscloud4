package com.sdg.cmdb.extend;

/**
 * Created by zxxiao on 2017/5/15.
 */
public interface Invocation {
    /**
     * 获取指定key 的string value
     * @param key
     * @return
     */
    String getParameterForString(String key);
}
