package com.baiyi.opscloud.datasource.kubernetes.util;

import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/8 9:35 AM
 * @Version 1.0
 */
public class KubernetesUtil {

    private KubernetesUtil(){}

    public static HasMetadata toResource(KubernetesClient kuberClient, String content) throws RuntimeException {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        List<HasMetadata> resources = kuberClient.load(is).get();
        if (resources.isEmpty()) // 配置文件为空
            throw new KubernetesException("转换Deployment配置文件错误!");
        return resources.get(0);
    }

}
