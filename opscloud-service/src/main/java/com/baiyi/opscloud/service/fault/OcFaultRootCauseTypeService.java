package com.baiyi.opscloud.service.fault;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultRootCauseType;
import com.baiyi.opscloud.domain.param.fault.FaultParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 4:19 下午
 * @Since 1.0
 */
public interface OcFaultRootCauseTypeService {

    void addOcFaultRootCauseType(OcFaultRootCauseType ocFaultRootCauseType);

    DataTable<OcFaultRootCauseType> queryOcFaultRootCauseTypePage(FaultParam.RootCauseTypePageQuery pageQuery);

    OcFaultRootCauseType queryOcFaultRootCauseType(int id);
}
