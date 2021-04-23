package com.baiyi.opscloud.builder.fault;

import com.baiyi.opscloud.common.base.FaultResponsibleType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultAction;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultResponsible;
import com.baiyi.opscloud.domain.vo.fault.FaultVO;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 6:18 下午
 * @Since 1.0
 */
public class OcFaultBuilder {

    public static List<OcFaultResponsible> FaultResponsibleListBuild(FaultVO.FaultInfo faultInfo) {
        List<OcFaultResponsible> ocFaultResponsibleList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(faultInfo.getPrimaryResponsiblePersonIdList())) {
            faultInfo.getPrimaryResponsiblePersonIdList().forEach(ocUserId -> {
                OcFaultResponsible ocFaultResponsible = new OcFaultResponsible();
                ocFaultResponsible.setResponsiblePerson(ocUserId);
                ocFaultResponsible.setFaultId(faultInfo.getId());
                ocFaultResponsible.setResponsibleType(FaultResponsibleType.primaryResponsiblePerson.getType());
                ocFaultResponsibleList.add(ocFaultResponsible);
            });
        }
        if (!CollectionUtils.isEmpty(faultInfo.getSecondaryResponsiblePersonIdList())) {
            faultInfo.getSecondaryResponsiblePersonIdList().forEach(ocUserId -> {
                OcFaultResponsible ocFaultResponsible = new OcFaultResponsible();
                ocFaultResponsible.setResponsiblePerson(ocUserId);
                ocFaultResponsible.setFaultId(faultInfo.getId());
                ocFaultResponsible.setResponsibleType(FaultResponsibleType.secondaryResponsiblePerson.getType());
                ocFaultResponsibleList.add(ocFaultResponsible);
            });
        }
        return ocFaultResponsibleList;
    }

    public static List<OcFaultAction> FaultActionListBuild(FaultVO.FaultInfo faultInfo) {
        if (CollectionUtils.isEmpty(faultInfo.getTodoAction()))
            return Collections.emptyList();
        List<OcFaultAction> ocFaultActionList = Lists.newArrayListWithCapacity(faultInfo.getTodoAction().size());
        faultInfo.getTodoAction().forEach(faultAction -> {
            OcFaultAction ocFaultAction = BeanCopierUtils.copyProperties(faultAction, OcFaultAction.class);
            ocFaultAction.setFaultId(faultInfo.getId());
            ocFaultActionList.add(ocFaultAction);
        });
        return ocFaultActionList;
    }
}
