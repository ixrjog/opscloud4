package com.baiyi.opscloud.task;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.task.util.TaskUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/8 1:37 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseTask {

    @Resource
    protected TaskUtil taskUtil;

    @Resource
    protected RedisUtil redisUtil;

//    protected void sleep(int maxSleep) {
//        try {
//            Thread.sleep(RandomUtils.acqRandom(maxSleep));//等进程执行一会，再终止它
//        } catch (InterruptedException ignored) {
//        }
//    }

}
