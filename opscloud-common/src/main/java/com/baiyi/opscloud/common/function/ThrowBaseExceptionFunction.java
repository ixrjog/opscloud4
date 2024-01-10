package com.baiyi.opscloud.common.function;

import com.baiyi.opscloud.common.exception.BaseException;

/**
 * @Author 修远
 * @Date 2023/5/8 2:53 PM
 * @Since 1.0
 */
@FunctionalInterface
public interface ThrowBaseExceptionFunction {

    /**
     * 抛出异常信息
     *
     * @param baseException 异常信息
     * @return void
     **/
    void throwBaseException(BaseException baseException);

}