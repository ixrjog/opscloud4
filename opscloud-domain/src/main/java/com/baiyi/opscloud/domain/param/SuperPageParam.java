package com.baiyi.opscloud.domain.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/12/31 9:36 AM
 * @Version 1.0
 */
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema
public class SuperPageParam {

    @Schema(description = "分页页码")
    @Builder.Default
    private Integer page = 1;

    @Max(value = 1024, message = "分页查询最大限制1024条记录")
    @Schema(description = "分页页长", example = "10")
    private Integer length;

}