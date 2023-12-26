package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.domain.param.kubernetes.BaseKubernetesParam;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/7/6 11:35
 * @Version 1.0
 */
@Slf4j
public class KubernetesIngressDriver {

    public static Ingress get(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IResource resource) {
        return get(kubernetes, resource.getNamespace(), resource.getName());
    }

    public static Ingress get(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .withName(name)
                    .item();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Ingress> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Ingress get(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Ingress ingress = toIngress(kc, content);
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata().getNamespace())
                    .resource(ingress)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Ingress create(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IStreamResource streamResource) {
        return create(kubernetes, streamResource.getResourceYaml());
    }

    public static Ingress create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Ingress ingress = toIngress(kc, content);
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata().getNamespace())
                    .resource(ingress)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Ingress update(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IStreamResource streamResource) {
        return update(kubernetes, streamResource.getResourceYaml());
    }

    public static Ingress update(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Ingress ingress = toIngress(kc, content);
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata().getNamespace())
                    .resource(ingress)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Ingress update(KubernetesConfig.Kubernetes kubernetes, Ingress ingress) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata().getNamespace())
                    .resource(ingress)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static Ingress toIngress(KubernetesClient kubernetesClient, String content) {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        return kubernetesClient
                .network()
                .v1()
                .ingresses()
                .load(is)
                .item();
    }

}