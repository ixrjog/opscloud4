package com.baiyi.opscloud.decorator.fault;

import com.baiyi.opscloud.common.base.FaultResponsibleType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeAgoUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.fault.FaultVO;
import com.baiyi.opscloud.service.fault.OcFaultActionService;
import com.baiyi.opscloud.service.fault.OcFaultInfoService;
import com.baiyi.opscloud.service.fault.OcFaultResponsibleService;
import com.baiyi.opscloud.service.fault.OcFaultRootCauseTypeService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 5:38 下午
 * @Since 1.0
 */

@Component("FaultDecorator")
public class FaultDecorator {

    private static Map<Integer, OcFaultInfo> faultInfoMap = Maps.newHashMap();

    @Resource
    private OcFaultResponsibleService ocFaultResponsibleService;

    @Resource
    private OcFaultActionService ocFaultActionService;

    @Resource
    private OcFaultRootCauseTypeService ocFaultRootCauseTypeService;

    @Resource
    private OcFaultInfoService ocFaultInfoService;

    @Resource
    private OcUserService ocUserService;

    public List<FaultVO.FaultInfo> decoratorInfoVOList(List<OcFaultInfo> ocFaultInfoList) {
        List<FaultVO.FaultInfo> faultInfoList = Lists.newArrayListWithCapacity(ocFaultInfoList.size());
        ocFaultInfoList.forEach(ocFaultInfo -> faultInfoList.add(decoratorSimpleInfoVO(ocFaultInfo)));
        return faultInfoList;
    }

    private FaultVO.FaultInfo decoratorSimpleInfoVO(OcFaultInfo ocFaultInfo) {
        FaultVO.FaultInfo faultInfo = BeanCopierUtils.copyProperties(ocFaultInfo, FaultVO.FaultInfo.class);
        OcFaultRootCauseType rootCauseType = ocFaultRootCauseTypeService.queryOcFaultRootCauseType(ocFaultInfo.getRootCauseTypeId());
        faultInfo.setRootCauseType(rootCauseType.getRootCauseType());
        faultInfo.setAgo(TimeAgoUtils.format(ocFaultInfo.getStartTime()));
        return faultInfo;
    }

    public FaultVO.FaultInfo decoratorInfoVO(OcFaultInfo ocFaultInfo) {
        FaultVO.FaultInfo faultInfo = decoratorSimpleInfoVO(ocFaultInfo);
        List<OcFaultAction> ocFaultActionList = ocFaultActionService.queryOcFaultActionByFaultId(ocFaultInfo.getId());
        List<FaultVO.FaultAction> todoAction = decoratorActionVOList(ocFaultActionList);
        faultInfo.setTodoAction(todoAction);
        List<OcUser> primaryResponsiblePerson = Lists.newArrayList();
        List<Integer> primaryResponsiblePersonIdList = Lists.newArrayList();
        List<OcUser> secondaryResponsiblePerson = Lists.newArrayList();
        List<Integer> secondaryResponsiblePersonIdList = Lists.newArrayList();
        List<OcFaultResponsible> ocFaultResponsibleList = ocFaultResponsibleService.queryOcFaultResponsibleByFaultId(ocFaultInfo.getId());
        ocFaultResponsibleList.forEach(ocFaultResponsible -> {
            OcUser ocUser = ocUserService.queryOcUserById(ocFaultResponsible.getResponsiblePerson());
            if (ocFaultResponsible.getResponsibleType().equals(FaultResponsibleType.primaryResponsiblePerson.getType())) {
                primaryResponsiblePerson.add(ocUser);
                primaryResponsiblePersonIdList.add(ocUser.getId());
            }
            if (ocFaultResponsible.getResponsibleType().equals(FaultResponsibleType.secondaryResponsiblePerson.getType())) {
                secondaryResponsiblePerson.add(ocUser);
                secondaryResponsiblePersonIdList.add(ocUser.getId());
            }
        });
        faultInfo.setPrimaryResponsiblePerson(primaryResponsiblePerson);
        faultInfo.setPrimaryResponsiblePersonIdList(primaryResponsiblePersonIdList);
        faultInfo.setSecondaryResponsiblePerson(secondaryResponsiblePerson);
        faultInfo.setSecondaryResponsiblePersonIdList(secondaryResponsiblePersonIdList);
        return faultInfo;
    }

    public List<FaultVO.FaultAction> decoratorActionVOList(List<OcFaultAction> ocFaultActionList) {
        List<FaultVO.FaultAction> faultActionList = Lists.newArrayListWithCapacity(ocFaultActionList.size());
        ocFaultActionList.forEach(ocFaultAction -> faultActionList.add(decoratorActionVO(ocFaultAction)));
        return faultActionList;
    }

    private FaultVO.FaultAction decoratorActionVO(OcFaultAction ocFaultAction) {
        FaultVO.FaultAction faultInfo = BeanCopierUtils.copyProperties(ocFaultAction, FaultVO.FaultAction.class);
        OcUser followUser = ocUserService.queryOcUserById(ocFaultAction.getFollowUserId());
        faultInfo.setFollowUser(followUser);
        if (!faultInfoMap.containsKey(ocFaultAction.getFaultId())) {
            OcFaultInfo ocFaultInfo = ocFaultInfoService.queryOcFaultInfo(ocFaultAction.getFaultId());
            faultInfoMap.put(ocFaultAction.getFaultId(), ocFaultInfo);
        }
        faultInfo.setFaultTitle(faultInfoMap.get(ocFaultAction.getFaultId()).getFaultTitle());
        return faultInfo;
    }

    public FaultVO.InfoMonthStats decoratorMonthStatsVO(List<FaultVO.InfoMonthStatsData> monthStatisticsList) {
        FaultVO.InfoMonthStats monthStatistics = new FaultVO.InfoMonthStats();
        if (CollectionUtils.isEmpty(monthStatisticsList)) {
            return monthStatistics;
        }
        List<String> dateCat = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> p0 = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> p1 = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> p2 = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> p3 = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> p4 = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> noFault = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        List<Integer> unrated = Lists.newArrayListWithCapacity(monthStatisticsList.size());
        monthStatisticsList.forEach(statistics -> {
            dateCat.add(statistics.getDateCat());
            p0.add(statistics.getP0());
            p1.add(statistics.getP1());
            p2.add(statistics.getP2());
            p3.add(statistics.getP3());
            p4.add(statistics.getP4());
            noFault.add(statistics.getNoFault());
            unrated.add(statistics.getUnrated());
        });
        monthStatistics.setDateCat(dateCat);
        monthStatistics.setP0(p0);
        monthStatistics.setP1(p1);
        monthStatistics.setP2(p2);
        monthStatistics.setP3(p3);
        monthStatistics.setP4(p4);
        monthStatistics.setNoFault(noFault);
        monthStatistics.setUnrated(unrated);
        return monthStatistics;
    }
}
