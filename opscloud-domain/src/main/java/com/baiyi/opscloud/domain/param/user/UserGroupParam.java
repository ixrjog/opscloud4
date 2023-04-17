package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:24 下午
 * @Version 1.0
 */
public class UserGroupParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class UserGroupPageQuery extends PageParam implements IExtend {

        @Schema(description = "模糊查询")
        private String queryName;

        @Schema(description = "展开")
        private Boolean extend;

        @Schema(description = "是否激活")
        private Boolean isActive;
    }

}
