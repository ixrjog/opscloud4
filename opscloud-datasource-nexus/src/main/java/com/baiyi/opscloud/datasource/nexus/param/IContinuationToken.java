package com.baiyi.opscloud.datasource.nexus.param;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2023/4/23 17:23
 * @Version 1.0
 */
public interface IContinuationToken {

    String getContinuationToken();

    default boolean isContinuation() {
        return StringUtils.isNoneBlank(getContinuationToken());
    }

}