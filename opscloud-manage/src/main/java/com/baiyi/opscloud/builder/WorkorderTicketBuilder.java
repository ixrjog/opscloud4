package com.baiyi.opscloud.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.bo.workorder.WorkorderTicketBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;

/**
 * @Author baiyi
 * @Date 2020/4/28 11:10 上午
 * @Version 1.0
 */
public class WorkorderTicketBuilder {

    public static OcWorkorderTicket build(OcUser ocUser, OcWorkorder ocWorkorder) {
        WorkorderTicketBO workorderTicketBO = WorkorderTicketBO.builder()
                .workorderId(ocWorkorder.getId())
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .userDetail(JSON.toJSONString(ocUser))
                .build();
        return covert(workorderTicketBO);
    }

    private static OcWorkorderTicket covert(WorkorderTicketBO workorderTicketBO) {
        return BeanCopierUtils.copyProperties(workorderTicketBO, OcWorkorderTicket.class);
    }
}
