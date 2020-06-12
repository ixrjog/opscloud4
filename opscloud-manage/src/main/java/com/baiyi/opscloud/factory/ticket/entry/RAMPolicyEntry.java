package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/6/11 3:02 下午
 * @Version 1.0
 */
@Builder
@Data
public class RAMPolicyEntry implements ITicketEntry {

    private AliyunRAMVO.RAMPolicy ramPolicy;

    public String getName() {
        return this.ramPolicy.getPolicyName();
    }

}
