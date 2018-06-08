package com.sdg.cmdb.util;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class CmdUtilsTest {

    @Test
    public void test() {
        //TODO 这里写用例代码


        CommandLine cmd =new CommandLine("ps");
        cmd.addArgument("-ef");
        String rt=CmdUtils.run(cmd);


        //String rt=CmdUtils.runCmd("ps -ef");
        System.err.println(rt);
        //int exitCode=CmdUtils.callShell("ps -ef");
        //System.out.println(exitCode);
    }


}
