package com.baiyi.opscloud.loop.kubernetes.factory;

import com.baiyi.opscloud.loop.kubernetes.IKubernetesDeploymentRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2023/3/20 17:07
 * @Version 1.0
 */
@Slf4j
public class KubernetesDeploymentMessageHandlerFactory {

    static Map<String, IKubernetesDeploymentRequestHandler> context = new ConcurrentHashMap<>();

    public static IKubernetesDeploymentRequestHandler getHandlerByMessageType(String messageType) {
        return context.get(messageType);
    }

    public static void register(IKubernetesDeploymentRequestHandler bean) {
        log.debug("KubernetesDeploymentMessageHandlerFactory  Registered: messageType={}", bean.getMessageType());
        context.put(bean.getMessageType(), bean);
    }

}
