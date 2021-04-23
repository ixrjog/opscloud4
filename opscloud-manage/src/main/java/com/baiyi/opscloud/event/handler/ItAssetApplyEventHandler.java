package com.baiyi.opscloud.event.handler;

import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.event.listener.ItAssetApplyEventListener;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/1 3:25 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ItAssetApplyEventHandler {

    @Resource
    private AsyncEventBus asyncEventBus;

    @Resource
    private ItAssetApplyEventListener itAssetApplyEventListener;

    @PostConstruct
    public void init() {
        asyncEventBus.register(itAssetApplyEventListener);
    }

    @PreDestroy
    public void destroy() {
        asyncEventBus.unregister(itAssetApplyEventListener);
    }

    public void eventPost(ItAssetParam.ApplyAsset applyAsset) {
        asyncEventBus.post(applyAsset);
        log.info("IT资产派发事件触发，资产id:{}", applyAsset.getAssetId());
    }
}
