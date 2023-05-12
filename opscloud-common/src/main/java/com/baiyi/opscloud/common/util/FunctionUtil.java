package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.common.function.BranchFunction;
import com.baiyi.opscloud.common.function.ThrowBaseExceptionFunction;

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

    public static BranchFunction isTureOrFalse(boolean b) {

        return (trueHandle, falseHandle) -> {
            if (b) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

}
