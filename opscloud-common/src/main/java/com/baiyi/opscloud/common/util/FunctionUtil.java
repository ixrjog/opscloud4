package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.function.BranchFunction;
import com.baiyi.opscloud.common.function.ThrowBaseExceptionFunction;
import com.baiyi.opscloud.common.function.TrueFunction;
import com.google.common.base.Strings;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @Author 修远
 * @Date 2023/5/8 2:55 PM
 * @Since 1.0
 */
public class FunctionUtil {

    public static ThrowBaseExceptionFunction isTure(boolean b) {
        return (baseException) -> {
            if (b) {
                throw baseException;
            }
        };
    }

    public static ThrowBaseExceptionFunction isNullOrEmpty(String s) {
        return (baseException) -> {
            if (Strings.isNullOrEmpty(s)) {
                throw baseException;
            }
        };
    }

    public static ThrowBaseExceptionFunction isNull(Object o) {
        return (baseException) -> {
            if (ObjectUtils.isEmpty(o)) {
                throw baseException;
            }
        };
    }

    public static ThrowBaseExceptionFunction isNotNull(Object o) {
        return (baseException) -> {
            if (ObjectUtils.isNotEmpty(o)) {
                throw baseException;
            }
        };
    }

    public static BranchFunction isTureOrFalse(boolean b) {
        return (trueHandle, falseHandle) -> {
            if (b) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    public static TrueFunction trueFunction(boolean b) {
        return (trueHandle) -> {
            if (b) {
                trueHandle.run();
            }
        };
    }

}