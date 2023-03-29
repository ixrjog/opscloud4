package com.baiyi.opscloud.datasource.kubernetes.event;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import io.fabric8.kubernetes.api.builder.Visitor;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2021/7/29 4:56 下午
 * @Version 1.0
 */
@Slf4j
public class KubernetesWatchEvent {

    public static void watch(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (
                KubernetesClient client = MyKubernetesClientBuilder.build(kubernetes);
                Watch ignored = newConfigMapWatch(client)
        ) {
            final String name = "watch-config-map-test-" + UUID.randomUUID().toString();
            final ConfigMap cm = client.configMaps().inNamespace(namespace).createOrReplace(new ConfigMapBuilder()
                    .withNewMetadata().withName(name).endMetadata()
                    .build()
            );
            client.configMaps().inNamespace(namespace).withName(name)
                    .patch(new ConfigMapBuilder().withNewMetadata().withName(name).endMetadata().addToData("key", "value").build());
            // noinspection Convert2Lambda
            client.configMaps().inNamespace(namespace).withName(name).edit(new Visitor<ObjectMetaBuilder>() {
                @Override
                public void visit(ObjectMetaBuilder omb) {
                    omb.addToAnnotations("annotation", "value");
                }
            });
            client.configMaps().delete(cm);
        } catch (Exception e) {
            log.error("Global Error: {}", e.getMessage(), e);
        }
    }

    private static Watch newConfigMapWatch(KubernetesClient client) {

        return client.configMaps().watch(new Watcher<ConfigMap>() {
            @Override
            public void eventReceived(Action action, ConfigMap resource) {
                log.info("Watch event received {}: {}", action.name(), resource.getMetadata().getName());
            }

            @Override
            public void onClose(WatcherException e) {
                log.error("Watch error received: {}", e.getMessage(), e);
            }

            @Override
            public void onClose() {
                log.info("Watch gracefully closed");
            }
        });
    }

}
