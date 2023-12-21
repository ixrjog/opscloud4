package com.baiyi.opscloud.common.function;

/**
 * @Author 修远
 * @Date 2023/5/8 3:03 PM
 * @Since 1.0
 */

@FunctionalInterface
public interface BranchFunction {

    /**
     * 分支操作
     * @param withTrue
     * @param withFalse
     */
    void withBoolean(Runnable withTrue, Runnable withFalse);


}