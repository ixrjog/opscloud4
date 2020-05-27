package com.baiyi.opscloud.factory.change.impl;

import com.baiyi.opscloud.factory.change.IServerChange;
import com.baiyi.opscloud.factory.change.ServerChangeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:14 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServerChange implements IServerChange , InitializingBean {




    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        ServerChangeFactory.register(this);
    }
}
