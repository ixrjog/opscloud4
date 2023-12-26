package com.baiyi.opscloud.datasource.kubernetes.event;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @Author baiyi
 * @Date 2021/7/29 5:31 下午
 * @Version 1.0
 */
@Slf4j
public class KubernetesPodWatch {

    public static void watch(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        // Latch for Watch termination
        final CountDownLatch isWatchClosed = new CountDownLatch(1);
        try (final KubernetesClient k8s = MyKubernetesClientBuilder.build(kubernetes)) {
             k8s.pods().inNamespace(namespace).
                    watch(new Watcher<Pod>() {
                @Override
                public void eventReceived(Action action, Pod pod) {
                    log.info("{} {}", action.name(), pod.getMetadata().getName());
                    switch (action) {
                        case ADDED:
                            log.info("{} got added", pod.getMetadata().getName());
                            break;
                        case DELETED:
                            log.info("{} got deleted", pod.getMetadata().getName());
                            break;
                        case MODIFIED:
                            log.info("{} got modified", pod.getMetadata().getName());
                            break;
                        default:
                            log.error("Unrecognized event: {}", action.name());
                    }
                }
                @Override
                public void onClose(WatcherException e) {
                    log.info("Closed");
                    isWatchClosed.countDown();
                }
            });

            // Wait till watch gets closed
            System.err.println("111111111111111111");
            isWatchClosed.await();
            System.err.println("222222222222222222");
        } catch (InterruptedException interruptedException) {
            log.warn("Interrupted while waiting for the watch to close: {}", interruptedException.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}