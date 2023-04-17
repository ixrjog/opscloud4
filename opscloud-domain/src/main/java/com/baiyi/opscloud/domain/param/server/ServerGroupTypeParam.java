package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 1:18 下午
 * @Version 1.0
 */
public class ServerGroupTypeParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerGroupTypePageQuery extends PageParam implements IExtend {

        @Schema(description = "名称")
        private String name;

        private Boolean extend;

    }

}