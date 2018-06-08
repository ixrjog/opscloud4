package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.plugin.chain.TaskCallback;
import com.sdg.cmdb.plugin.chain.TaskChain;
import com.sdg.cmdb.util.CmdUtils;
import org.apache.commons.exec.CommandLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/8/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AnsibleTaskServiceTest {

    @Resource
    private AnsibleTaskService ansibleTaskService;

    @Test
    public void testAcqConfigByServerAndKey() {
        TaskCallback taskCallback = new TaskCallback() {
            @Override
            public void doNotify(Object notify) {
                System.err.println("xxx");
            }

        };
       // TaskChain.TaskItem taskItem = new TaskChain.TaskItem("xxx", taskCallback);
    }


    @Test
    public void testCommandArgs() {
        CommandLine c = new CommandLine("/usr/local/Cellar/ansible/2.4.3.0/libexec/bin/ansible");
        c.addArgument("--version");

//        c.addArgument("-ef");
//        c.addArgument("|");
//        c.addArgument("grep a");
        for(String s:c.getArguments())
        System.err.println(s);


        String rt = CmdUtils.run(c);
        System.err.println(rt);

    }


}
