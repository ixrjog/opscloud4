package com.baiyi.opscloud.common.function;

/**
 * @Author 修远
 * @Date 2023/5/16 2:58 PM
 * @Since 1.0
 */

@FunctionalInterface
public interface TrueFunction {

    void withTrue(Runnable runnable);

}