package com.baiyi.caesar.common.exception.build;

import com.baiyi.caesar.common.exception.BaseException;
import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/26 4:45 下午
 * @Since 1.0
 */
public class BuildRuntimeException extends BaseException {

    public BuildRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}
