package com.baiyi.opscloud.leo.handler.build.base;

import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2023/4/20 11:18
 * @Version 1.0
 */
public abstract class BaseBuildStrategy extends BaseBuildChainHandler implements IBuildStrategy, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        BuildStrategyFactory.register(this);
    }

}