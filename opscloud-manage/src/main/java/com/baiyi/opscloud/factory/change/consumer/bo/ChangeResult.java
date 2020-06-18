package com.baiyi.opscloud.factory.change.consumer.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/29 5:20 下午
 * @Version 1.0
 */
@Data
@Builder
public class ChangeResult {

    @Builder.Default
    private int code = 1 ;
    @Builder.Default
    private String msg = "ERROR";

}
