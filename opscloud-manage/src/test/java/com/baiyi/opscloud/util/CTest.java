package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.workorder.helper.ContainerJvmSpecHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/6/6 12:57
 * @Version 1.0
 */
@Slf4j
public class CTest extends BaseUnit {

    // -Xms4096M -Xmx4096M -Xmn2048M -XX:MetaspaceSize=128M

    public static final String JAVA_OPTS_TEST = """
   
                            -XX:MaxMetaspaceSize=256M -Xss256K
                            -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps
                            -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC
                            -XX:+UseCMSInitiatingOccupancyOnly
                            -XX:CMSInitiatingOccupancyFraction=80
            """;

    public static final String JAVA_OPTS_TEST2 = """
                            -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps
                            -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC
                            -XX:+UseCMSInitiatingOccupancyOnly
                            -XX:CMSInitiatingOccupancyFraction=80
                            -XX:MaxMetaspaceSize=256M -Xss256K
            """;

    @Test
    void ddd() {
        // 2XLARGE
        List<String> args = ContainerJvmSpecHelper.parse("XLARGE2", JAVA_OPTS_TEST2);
        print(ContainerJvmSpecHelper.format(args));

    }

    @Test
    void test() {
        print(String.format("|%-15s|", "1.1.1.1"));
        print(String.format("|%-15s|", "255.255.255.255"));
    }


    @Test
    void funTest() {
        try {
            FunctionUtil.isTure(false).throwBaseException(new OCException("工具类测试-false"));
            FunctionUtil.isTure(true).throwBaseException(new OCException("工具类测试-true"));
        } catch (OCException ocException) {
            print(ocException.getMessage());
        }

        FunctionUtil.isTureOrFalse(true)
                .withBoolean(
                        () -> print("trueHandle-true"),
                        () -> print("falseHandle-true")
                );

        FunctionUtil.isTureOrFalse(false)
                .withBoolean(
                        () -> print("trueHandle-false"),
                        () -> print("falseHandle-false")
                );
    }



}
