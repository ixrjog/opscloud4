package com.baiyi.opscloud.domain.param.terminal;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:23 下午
 * @Version 1.0
 */
public class TerminalSessionParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class TerminalSessionPageQuery extends PageParam implements IExtend {

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "会话类型")
        private String sessionType;

        @Schema(description = "会话状态")
        private Boolean sessionClosed;

        @Schema(description = "Opscloud实例名称")
        private String serverHostname;

        private Boolean extend;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class BatchCloseTerminalSession  {

        @Schema(description = "查询参数")
        private List<Integer> ids;

    }

}