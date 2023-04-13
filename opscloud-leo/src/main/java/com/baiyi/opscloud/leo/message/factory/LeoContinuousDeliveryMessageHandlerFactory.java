package com.baiyi.opscloud.leo.message.factory;

import com.baiyi.opscloud.leo.message.handler.base.ILeoContinuousDeliveryRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2022/11/23 15:50
 * @Version 1.0
 */
@Slf4j
public class LeoContinuousDeliveryMessageHandlerFactory {

    static Map<String, ILeoContinuousDeliveryRequestHandler> context = new ConcurrentHashMap<>();

    public static ILeoContinuousDeliveryRequestHandler getHandlerByMessageType(String messageType) {
        return context.get(messageType);
    }

    public static void register(ILeoContinuousDeliveryRequestHandler bean) {
        log.debug("LeoContinuousDeliveryMessageHandlerFactory Registered: messageType={}", bean.getMessageType());
        context.put(bean.getMessageType(), bean);
    }

}