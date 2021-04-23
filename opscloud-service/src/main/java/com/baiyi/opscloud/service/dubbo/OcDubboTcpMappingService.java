package com.baiyi.opscloud.service.dubbo;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboTcpMapping;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/14 10:48 上午
 * @Version 1.0
 */
public interface OcDubboTcpMappingService {

    OcDubboTcpMapping queryOcDubboTcpMappingByMaxPort();

    OcDubboTcpMapping queryOcDubboTcpMappingByUniqueKey(OcDubboTcpMapping ocDubboTcpMapping);

    List<OcDubboTcpMapping> queryOcDubboTcpMappingByEnv(String env);

    void addOcDubboTcpMapping(OcDubboTcpMapping ocDubboTcpMapping);

    void updateOcDubboTcpMapping(OcDubboTcpMapping ocDubboTcpMapping);
}
