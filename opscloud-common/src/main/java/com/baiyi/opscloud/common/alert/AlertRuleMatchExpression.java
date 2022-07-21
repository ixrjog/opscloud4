package com.baiyi.opscloud.common.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/7/21 11:02 AM
 * @Since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRuleMatchExpression {

    private Integer weight;
    private String severity;
    private String operator;
    private String values;
}
