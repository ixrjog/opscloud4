package com.baiyi.opscloud.domain.param;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/8/19 2:41 下午
 * @Version 1.0
 */
@Builder
@Data
public class SimpleExtend implements IExtend {

    public static final SimpleExtend EXTEND = SimpleExtend.builder().extend(true).build();

    public static final SimpleExtend NOT_EXTEND = SimpleExtend.builder().extend(false).build();

    private Boolean extend;

}