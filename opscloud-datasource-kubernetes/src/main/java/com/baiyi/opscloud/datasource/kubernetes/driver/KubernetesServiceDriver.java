package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.param.kubernetes.BaseKubernetesParam;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:55 下午
 * @Version 1.0
 */
@Slf4j
public class KubernetesServiceDriver {

    public static List<Service> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            ServiceList serviceList = kc.services()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(serviceList.getItems())) {
                return Collections.emptyList();
            }
            return serviceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Service get(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IResource resource) {
        return get(kubernetes, resource.getNamespace(), resource.getName());
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Service get(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<StatusDetails> delete(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IResource resource) {
        return delete(kubernetes, resource.getNamespace(), resource.getName());
    }

    public static List<StatusDetails> delete(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(namespace)
                    .withName(name)
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Service create(KubernetesConfig.Kubernetes kubernetes, Service service) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(service.getMetadata().getNamespace())
                    .resource(service)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Service create(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IStreamResource streamResource) {
        return create(kubernetes, streamResource.getResourceYaml());
    }

    public static Service create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Service service = toService(kc, content);
            return create(kubernetes, service);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Service update(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IStreamResource streamResource) {
        return update(kubernetes, streamResource.getResourceYaml());
    }

    public static Service update(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Service service = toService(kc, content);
            return update(kubernetes, service);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static Service update(KubernetesConfig.Kubernetes kubernetes, Service service) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(service.getMetadata().getNamespace())
                    .resource(service)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Service get(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Service service = toService(kc, content);
            return get(kubernetes, service);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static Service get(KubernetesConfig.Kubernetes kubernetes, Service service) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(service.getMetadata().getNamespace())
                    .resource(service)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 配置文件转换为服务资源
     *
     * @param kubernetesClient
     * @param content          YAML
     * @return
     * @throws RuntimeException
     */
    private static Service toService(KubernetesClient kubernetesClient, String content) throws KubernetesException {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        return kubernetesClient
                .services()
                .load(is)
                .item();
    }

}