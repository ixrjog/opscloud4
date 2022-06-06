package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/6/6 12:57
 * @Version 1.0
 */
public class CTest extends BaseUnit {

    @Test
    void test(){
        print(String.format("|%-15s|", "1.1.1.1"));
        print(String.format("|%-15s|", "255.255.255.255"));
    }
}
