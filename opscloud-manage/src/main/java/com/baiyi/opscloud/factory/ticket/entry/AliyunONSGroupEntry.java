package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/27 1:46 下午
 * @Since 1.0
 */

@Data
public class AliyunONSGroupEntry implements ITicketEntry {

    private AliyunONSParam.GroupCreate group;

    @Override
    public String getName() {
        return group.getGroupId();
    }
}
