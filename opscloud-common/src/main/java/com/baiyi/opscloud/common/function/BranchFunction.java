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
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     * @return void
     **/
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);

}
