package com.baiyi.caesar.config;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author baiyi
 * @Date 2021/6/1 11:38 上午
 * @Version 1.0
 */
class WhiteConfigTest extends BaseUnit {

    @Resource
    private  WhiteConfig whiteConfig;

    @Test
    void d(){
        System.err.println(JSON.toJSON(whiteConfig));
    }

}