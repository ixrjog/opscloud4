package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2023/4/24 13:50
 * @Version 1.0
 */
public class BuildToolsVersionUtil {

    private BuildToolsVersionUtil() {
    }

    public void ddd(String line){
        // version '0.0.1-SNAPSHOT'

        String regex = "^s*version:";
        System.out.println(line.matches(regex));
    }

}
