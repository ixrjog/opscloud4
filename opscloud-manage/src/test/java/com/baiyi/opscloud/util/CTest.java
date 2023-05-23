package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.FunctionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/6/6 12:57
 * @Version 1.0
 */
@Slf4j
public class CTest extends BaseUnit {

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
                .trueOrFalseHandle(
                        () -> print("trueHandle-true"),
                        () -> print("falseHandle-true")
                );

        FunctionUtil.isTureOrFalse(false)
                .trueOrFalseHandle(
                        () -> print("trueHandle-false"),
                        () -> print("falseHandle-false")
                );
    }

}
