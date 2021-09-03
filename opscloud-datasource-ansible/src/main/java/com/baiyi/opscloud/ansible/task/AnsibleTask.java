package com.baiyi.opscloud.ansible.task;

import com.baiyi.opscloud.ansible.task.base.AbstractAnsibleTask;
import com.baiyi.opscloud.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;

/**
 * @Author baiyi
 * @Date 2021/9/2 4:44 下午
 * @Version 1.0
 */
@Slf4j
public class AnsibleTask extends AbstractAnsibleTask {

    private boolean stop = false;
    private CommandLine commandLine;
    private long timeout;

    // 100 分钟
    public static final long MAX_TIMEOUT = TimeUtil.minuteTime * 100;

    public AnsibleTask() {

    }

    @Override
    public void run() {

        final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

    }
}
