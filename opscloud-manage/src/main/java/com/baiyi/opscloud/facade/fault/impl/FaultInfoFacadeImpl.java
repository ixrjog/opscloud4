package com.baiyi.opscloud.facade.fault.impl;

import com.baiyi.opscloud.builder.fault.OcFaultBuilder;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.fault.FaultDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultAction;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultInfo;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultResponsible;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultRootCauseType;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import com.baiyi.opscloud.domain.vo.fault.FaultVO;
import com.baiyi.opscloud.facade.fault.FaultInfoFacade;
import com.baiyi.opscloud.service.fault.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 5:30 下午
 * @Since 1.0
 */
@Slf4j
@Component("FaultInfoFacade")
public class FaultInfoFacadeImpl implements FaultInfoFacade {

    @Resource
    private OcFaultInfoService ocFaultInfoService;

    @Resource
    private OcFaultActionService ocFaultActionService;

    @Resource
    private OcFaultResponsibleService ocFaultResponsibleService;

    @Resource
    private OcFaultRootCauseTypeService ocFaultRootCauseTypeService;

    @Resource
    private OcFaultInfoDashboardService ocFaultInfoDashboardService;

    @Resource
    private FaultDecorator faultDecorator;

    @Override
    public DataTable<FaultVO.FaultInfo> queryFaultInfoPage(FaultParam.InfoPageQuery pageQuery) {
        DataTable<OcFaultInfo> table = ocFaultInfoService.queryOcFaultInfoPage(pageQuery);
        List<FaultVO.FaultInfo> faultInfoList = faultDecorator.decoratorInfoVOList(table.getData());
        return new DataTable<>(faultInfoList, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> saveFaultInfo(FaultVO.FaultInfo faultInfo) {
        OcFaultInfo ocFaultInfo = BeanCopierUtils.copyProperties(faultInfo, OcFaultInfo.class);
        if (faultInfo.getId() == null) {
            try {
                ocFaultInfoService.addOcFaultInfo(ocFaultInfo);
                faultInfo.setId(ocFaultInfo.getId());
                saveFaultResponsiblePerson(faultInfo);
                saveFaultAction(faultInfo);
                return BusinessWrapper.SUCCESS;
            } catch (Exception e) {
                log.error("保存故障信息失败,故障标题为:{}", faultInfo.getFaultTitle(), e);
            }
        } else {
            saveFaultResponsiblePerson(faultInfo);
            saveFaultAction(faultInfo);
            ocFaultInfoService.updateOcFaultInfo(ocFaultInfo);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.SAVE_FAULT_INFO_FAIL);
    }

    private void saveFaultResponsiblePerson(FaultVO.FaultInfo faultInfo) {
        if (faultInfo.getId() != null)
            ocFaultResponsibleService.delOcFaultResponsibleByFaultId(faultInfo.getId());
        List<OcFaultResponsible> responsibleList = OcFaultBuilder.FaultResponsibleListBuild(faultInfo);
        try {
            ocFaultResponsibleService.addOcFaultResponsibleList(responsibleList);
        } catch (Exception e) {
            log.error("保存故障责任人失败", e);
        }
    }

    private void saveFaultAction(FaultVO.FaultInfo faultInfo) {
        List<OcFaultAction> ocFaultActionList = OcFaultBuilder.FaultActionListBuild(faultInfo);
        ocFaultActionList.forEach(this::saveFaultAction);
    }

    private void saveFaultAction(FaultVO.FaultAction faultAction) {
        OcFaultAction ocFaultAction = BeanCopierUtils.copyProperties(faultAction, OcFaultAction.class);
        saveFaultAction(ocFaultAction);
    }

    private void saveFaultAction(OcFaultAction ocFaultAction) {
        if (ocFaultAction.getId() == null) {
            try {
                ocFaultActionService.addOcFaultAction(ocFaultAction);
            } catch (Exception e) {
                log.error("保存故障Action失败", e);
            }
        } else {
            ocFaultActionService.updateOcFaultAction(ocFaultAction);
        }
    }

    @Override
    public BusinessWrapper<Boolean> updateFaultAction(FaultVO.FaultAction faultAction) {
        saveFaultAction(faultAction);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateFaultInfoFinalized(Integer id) {
        OcFaultInfo ocFaultInfo = ocFaultInfoService.queryOcFaultInfo(id);
        if (ocFaultInfo == null)
            return new BusinessWrapper<>(ErrorEnum.FAULT_INFO_NOT_EXIST);
        ocFaultInfo.setFinalized(!ocFaultInfo.getFinalized());
        ocFaultInfoService.updateOcFaultInfo(ocFaultInfo);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delFaultInfo(Integer id) {
        OcFaultInfo ocFaultInfo = ocFaultInfoService.queryOcFaultInfo(id);
        if (ocFaultInfo == null)
            return new BusinessWrapper<>(ErrorEnum.FAULT_INFO_NOT_EXIST);
        ocFaultInfoService.delOcFaultInfo(id);
        ocFaultActionService.delOcFaultActionByFaultId(id);
        ocFaultResponsibleService.delOcFaultResponsibleByFaultId(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OcFaultRootCauseType> queryFaultRootCauseTypePage(FaultParam.RootCauseTypePageQuery pageQuery) {
        return ocFaultRootCauseTypeService.queryOcFaultRootCauseTypePage(pageQuery);
    }

    @Override
    public BusinessWrapper<FaultVO.FaultInfo> queryFaultInfo(Integer id) {
        OcFaultInfo ocFaultInfo = ocFaultInfoService.queryOcFaultInfo(id);
        if (ocFaultInfo == null)
            return new BusinessWrapper<>(ErrorEnum.FAULT_INFO_NOT_EXIST);
        FaultVO.FaultInfo faultInfo = faultDecorator.decoratorInfoVO(ocFaultInfo);
        return new BusinessWrapper<>(faultInfo);
    }

    @Override
    public BusinessWrapper<FaultVO.InfoMonthStats> queryFaultInfoMonthStats(Integer queryYear) {
        List<FaultVO.InfoMonthStatsData> monthStatisticsList = ocFaultInfoDashboardService.queryFaultStatsMonth(queryYear);
        FaultVO.InfoMonthStats monthStatistics = faultDecorator.decoratorMonthStatsVO(monthStatisticsList);
        return new BusinessWrapper<>(monthStatistics);
    }

    @Override
    public BusinessWrapper<Boolean> addRootCauseType(FaultParam.AddRootCauseType param) {
        OcFaultRootCauseType ocFaultRootCauseType = new OcFaultRootCauseType();
        ocFaultRootCauseType.setRootCauseType(param.getRootCauseType());
        ocFaultRootCauseTypeService.addOcFaultRootCauseType(ocFaultRootCauseType);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<FaultVO.FaultAction> queryFaultActionPage(FaultParam.ActionPageQuery pageQuery) {
        DataTable<OcFaultAction> table = ocFaultActionService.queryFaultActionPage(pageQuery);
        List<FaultVO.FaultAction> faultActionList = faultDecorator.decoratorActionVOList(table.getData());
        return new DataTable<>(faultActionList, table.getTotalNum());
    }
}
