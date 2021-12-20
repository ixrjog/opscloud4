package com.baiyi.opscloud.common.exception.build;

import com.baiyi.opscloud.common.exception.BaseException;
import com.baiyi.opscloud.domain.ErrorEnum;

/**
 * @Author 修远
 * @Date 2021/3/26 4:45 下午
 * @Since 1.0
 */
public class BuildRuntimeException extends BaseException {

    public BuildRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}
