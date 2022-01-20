package com.baiyi.opscloud.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/1/20 3:19 PM
 * @Since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJob {

    private String name;
    private String group;
    private String status;
    private String description;
    private String cronExpression;

}
