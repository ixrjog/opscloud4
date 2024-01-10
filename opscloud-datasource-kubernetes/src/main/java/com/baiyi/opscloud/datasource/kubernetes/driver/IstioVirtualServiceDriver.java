package com.baiyi.opscloud.datasource.kubernetes.driver;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.client.istio.IstioClientBuilder;
import com.baiyi.opscloud.domain.param.kubernetes.BaseKubernetesParam;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.istio.api.networking.v1alpha3.VirtualServiceList;
import io.fabric8.istio.client.IstioClient;
import io.fabric8.kubernetes.api.model.StatusDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/10/7 16:25
 * @Version 1.0
 */
@Slf4j
public class IstioVirtualServiceDriver {

    public static VirtualService create(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IStreamResource streamResource) {
        return create(kubernetes, streamResource.getResourceYaml());
    }

    public static VirtualService create(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            VirtualService virtualService = ic.v1alpha3()
                    .virtualServices()
                    .load(is)
                    .item();
            return ic.v1alpha3()
                    .virtualServices()
                    .resource(virtualService)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static VirtualService update(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IStreamResource streamResource) {
        return update(kubernetes, streamResource.getResourceYaml());
    }

    public static VirtualService update(KubernetesConfig.Kubernetes kubernetes, String content) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            VirtualService virtualService = ic.v1alpha3()
                    .virtualServices()
                    .load(is)
                    .item();
            return ic.v1alpha3()
                    .virtualServices()
                    .resource(virtualService)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static List<VirtualService> list(KubernetesConfig.Kubernetes kubernetes, String namespace) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            VirtualServiceList virtualServiceList = ic.v1alpha3()
                    .virtualServices()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(virtualServiceList.getItems())) {
                return Collections.emptyList();
            }
            return virtualServiceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static VirtualService get(KubernetesConfig.Kubernetes kubernetes, BaseKubernetesParam.IResource resource) {
        return get(kubernetes, resource.getNamespace(), resource.getName());
    }

    public static VirtualService get(KubernetesConfig.Kubernetes kubernetes, String namespace, String name) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            return ic.v1alpha3()
                    .virtualServices()
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
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            return ic.v1alpha3()
                    .virtualServices()
                    .inNamespace(namespace)
                    .withName(name)
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}