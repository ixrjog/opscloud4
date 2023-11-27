package com.baiyi.opscloud.facade.kubernetes;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.kubernetes.IstioParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author baiyi
 * @Date 2023/11/27 11:23
 * @Version 1.0
 */
class IstioFacadeTest extends BaseUnit {

    @Resource
    private IstioFacade istioFacade;


    private static final String YAML = """
            ---
            apiVersion: "networking.istio.io/v1alpha3"
            kind: "VirtualService"
            metadata:
              name: "qa-data-center-daily"
              namespace: "daily"
            spec:
              hosts:
              - "qa-data-center"
              http:
              - route:
                - destination:
                    host: "qa-data-center"
                    subset: "stable"
            """;


    @Test
    void test(){

        IstioParam.UpdateResource updateResource = IstioParam.UpdateResource.builder()
                .resourceYaml(YAML)
                .instanceId(38)
                .build();


        istioFacade.updateIstioVirtualService(updateResource);

    }

}