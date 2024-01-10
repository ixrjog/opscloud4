package com.baiyi.opscloud.domain.vo.terminal;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/8/3 9:42 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceCommandVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Command extends BaseVO {

        private UserVO.User user;

        @Schema(description = "输出行数")
        private Integer outputRows;

        private Integer id;
        private Integer terminalSessionInstanceId;
        private String prompt;
        private Boolean isFormatted;
        private String input;
        private String inputFormatted;
        private String output;

    }

}