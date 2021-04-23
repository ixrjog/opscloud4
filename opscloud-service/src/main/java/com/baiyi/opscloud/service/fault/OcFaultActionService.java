package com.baiyi.opscloud.service.fault;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultAction;
import com.baiyi.opscloud.domain.param.fault.FaultParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 3:51 下午
 * @Since 1.0
 */
public interface OcFaultActionService {

    void addOcFaultActionList(List<OcFaultAction> ocFaultActionList);

    void addOcFaultAction(OcFaultAction ocFaultAction);

    void updateOcFaultAction(OcFaultAction ocFaultAction);

    List<OcFaultAction> queryOcFaultActionByFaultId(Integer faultId);

    void delOcFaultActionByFaultId(Integer faultId);

    DataTable<OcFaultAction> queryFaultActionPage(FaultParam.ActionPageQuery pageQuery);

}
