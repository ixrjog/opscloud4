package com.baiyi.opscloud.domain.vo.terminal;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class Command extends BaseVO {

        private UserVO.User user;

        @ApiModelProperty(value = "输出行数")
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
