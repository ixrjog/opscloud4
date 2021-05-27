package com.baiyi.caesar.util;

import com.baiyi.caesar.BaseUnit;
import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * @Author baiyi
 * @Date 2021/4/2 10:12 上午
 * @Version 1.0
 */
public class InstantTest extends BaseUnit {


    @Test
    void instantTest() {
        try {
            Instant inst1 = Instant.now();
            Thread.sleep(25);
            System.err.println(inst1);
           // Duration.between(inst1, Instant.now()).getSeconds`();
           // Instant.now().toEpochMilli();
           // inst1.toEpochMilli();
            System.err.println(Instant.now().toEpochMilli() -inst1.toEpochMilli() );

        } catch (Exception ex) {

        }
    }
}
