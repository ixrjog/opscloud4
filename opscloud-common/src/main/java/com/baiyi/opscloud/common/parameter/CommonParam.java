package com.baiyi.opscloud.common.parameter;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/2 9:46 上午
 * @Version 1.0
 */
@Data
@Builder
public class CommonParam {
    private String name;
    private String value;
}
