package com.baiyi.opscloud.facade.fault;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultRootCauseType;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import com.baiyi.opscloud.domain.vo.fault.FaultVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 5:29 下午
 * @Since 1.0
 */
public interface FaultInfoFacade {

    DataTable<FaultVO.FaultInfo> queryFaultInfoPage(FaultParam.InfoPageQuery pageQuery);

    BusinessWrapper<Boolean> saveFaultInfo(FaultVO.FaultInfo faultInfo);

    BusinessWrapper<Boolean> updateFaultAction(FaultVO.FaultAction faultAction);

    BusinessWrapper<Boolean> updateFaultInfoFinalized(Integer id);

    BusinessWrapper<Boolean> delFaultInfo(Integer id);

    DataTable<OcFaultRootCauseType> queryFaultRootCauseTypePage(FaultParam.RootCauseTypePageQuery pageQuery);

    BusinessWrapper<FaultVO.FaultInfo> queryFaultInfo(Integer id);

    BusinessWrapper<FaultVO.InfoMonthStats> queryFaultInfoMonthStats(Integer queryYear);

    BusinessWrapper<Boolean> addRootCauseType(FaultParam.AddRootCauseType param);

    DataTable<FaultVO.FaultAction> queryFaultActionPage(FaultParam.ActionPageQuery pageQuery);

}
