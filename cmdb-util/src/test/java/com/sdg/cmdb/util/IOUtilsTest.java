package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class IOUtilsTest {

    @Test
    public void test() {
        //TODO 这里写用例代码
        //IOUtils.build("testddddd" ,"/Users/liangjian/test.txt");
        String p=IOUtils.getPath("/data/www/ROOT/1.txt");
        System.out.println(p);

    }

}
