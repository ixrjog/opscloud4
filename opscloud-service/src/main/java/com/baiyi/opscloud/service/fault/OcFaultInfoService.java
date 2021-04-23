package com.baiyi.opscloud.service.fault;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultInfo;
import com.baiyi.opscloud.domain.param.fault.FaultParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 3:51 下午
 * @Since 1.0
 */
public interface OcFaultInfoService {

    OcFaultInfo queryOcFaultInfo(int id);

    void addOcFaultInfo(OcFaultInfo ocFaultInfo);

    void updateOcFaultInfo(OcFaultInfo ocFaultInfo);

    void delOcFaultInfo(int id);

    DataTable<OcFaultInfo> queryOcFaultInfoPage(FaultParam.InfoPageQuery pageQuery);

}
