package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.gatewayAdmin.AppSetVO;
import com.sdg.cmdb.domain.gatewayAdmin.GatewayAdminDO;

import java.util.List;

public interface GatewayAdminService {

    TableVO<List<GatewayAdminDO>> queryGatewayAdminPage(String serverGroupName, String appName, int page, int length);

    BusinessWrapper<Boolean> syncGatewayAdmin();

    AppSetVO preview(long id, int envType);

    BusinessWrapper<Boolean> appSet(long id, int envType);

    BusinessWrapper<Boolean> appServerSet(long id, int envType);

    /**
     * 批量设置
     * @return
     */
    BusinessWrapper<Boolean> batchSet();

    BusinessWrapper<Boolean> delGatewayadmin(long id);

    String appServerList(long id, int envType);
}
