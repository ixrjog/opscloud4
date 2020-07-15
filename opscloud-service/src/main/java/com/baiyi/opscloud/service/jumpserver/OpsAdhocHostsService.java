package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.OpsAdhocHosts;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 1:41 下午
 * @Version 1.0
 */
public interface OpsAdhocHostsService {

    void deleteOpsAdhocHostsById(int id);

    List<OpsAdhocHosts> queryOpsAdhocHostsByAssetId(String assetId);
}
