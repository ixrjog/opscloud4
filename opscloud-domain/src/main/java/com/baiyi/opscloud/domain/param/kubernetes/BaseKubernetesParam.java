package com.baiyi.opscloud.domain.param.kubernetes;

/**
 * @Author baiyi
 * @Date 2023/12/26 17:25
 * @Version 1.0
 */
public class BaseKubernetesParam {

    public interface IResource {

        Integer getInstanceId();

        String getNamespace();

        String getName();

    }

    public interface IStreamResource {

        Integer getInstanceId();

        String getResourceYaml();

    }

}