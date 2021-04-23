package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 11:44 上午
 * @Since 1.0
 */

@Data
public class KafkaGroupEntry implements ITicketEntry {

    private KafkaParam.GroupCreate group;

    @Override
    public String getName() {
        return group.getConsumerId();
    }
}
