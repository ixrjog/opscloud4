package com.baiyi.opscloud.domain.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
@Schema
public class PageParam {

    @Schema(description = "分页页码")
    private Integer page;

    @Max(value = 1024, message = "分页查询最大限制1024条记录")
    @Schema(description = "分页页长",example = "10")
    private Integer length;

}