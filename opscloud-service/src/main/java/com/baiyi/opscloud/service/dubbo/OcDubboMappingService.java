package com.baiyi.opscloud.service.dubbo;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMapping;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/9 4:08 下午
 * @Version 1.0
 */
public interface OcDubboMappingService {

    OcDubboMapping queryOcDubboMappingById(int id);

    /**
     * 查询一条未映射的数据
     * @return
     */
    OcDubboMapping queryOneOcDubboUnmappedByEnv(String env);

    List<OcDubboMapping> queryOcDubboMappingByEnv(String env);

    List<OcDubboMapping> queryOcDubboMappingByTcpMappingId(int tcpMappingId);

    OcDubboMapping queryOneOcDubboMappingByTcpMappingId(int tcpMappingId);

    void addOcDubboMapping(OcDubboMapping ocDubboMapping);

    void updateOcDubboMapping(OcDubboMapping ocDubboMapping);

    OcDubboMapping qeuryOcDubboMappingByUniqueKey(OcDubboMapping ocDubboMapping);

}
