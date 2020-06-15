package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/6/15 9:34 上午
 * @Version 1.0
 */
@Data
@Builder
public class AliyunLogMemberBO {

    private Integer id;
    private Integer logId;
    private Integer serverGroupId;
    private String serverGroupName;
    private String topic;
    private String comment;

}
