package com.baiyi.opscloud.factory.ticket.entry;

import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/27 1:43 下午
 * @Since 1.0
 */

@Data
public class AliyunONSTopicEntry implements ITicketEntry {

    private AliyunONSParam.TopicCreate topic;

    @Override
    public String getName() {
        return topic.getTopic();
    }
}
