package com.baiyi.opscloud.domain.param;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/9/7 4:28 下午
 * @Version 1.0
 */
@Builder
@Data
public class SimpleRelation implements IRelation {

    public static final SimpleRelation RELATION = SimpleRelation.builder().relation(true).build();

    public static final SimpleRelation NOT_RELATION = SimpleRelation.builder().relation(false).build();

    private Boolean relation;

}