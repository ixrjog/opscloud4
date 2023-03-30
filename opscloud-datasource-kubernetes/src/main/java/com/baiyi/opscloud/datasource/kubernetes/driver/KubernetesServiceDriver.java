package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.datasource.kubernetes.util.KubernetesUtil;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/25 3:55 下午
 * @Version 1.0
 */
@Slf4j
public class KubernetesServiceDriver {

    public static List<Service> listService(KubernetesConfig.Kubernetes kubernetes) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            ServiceList serviceList = kc.services()
                    .list();
            return serviceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<Service> listService(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            ServiceList serviceList = kc.services()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(serviceList.getItems()))
                return Collections.emptyList();
            return serviceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * @param kubernetes
     * @param namespace
     * @param name       podName
     * @return
     */
    public static Service getService(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
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

    public static Service createOrReplaceService(KubernetesConfig.Kubernetes kubernetes, Service service) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(service.getMetadata().getNamespace())
                    .createOrReplace(service);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static Service createOrReplaceService(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            Service service = toService(kc, content);
            return createOrReplaceService(kubernetes, service);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 配置文件转换为服务资源
     *
     * @param kubernetesClient
     * @param content     YAML
     * @return
     * @throws RuntimeException
     */
    public static Service toService(KubernetesClient kubernetesClient, String content) throws KubernetesException {
        return  KubernetesUtil.toService(kubernetesClient,content);
    }

}
