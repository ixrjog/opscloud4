package com.baiyi.opscloud.domain.base;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/8/27 11:24 上午
 * @Version 1.0
 */
@Builder
@Data
public class SimpleBusiness implements BaseBusiness.IBusiness {

    private Integer businessType;

    private Integer businessId;

}