package com.baiyi.opscloud.domain.notice.message;

import com.baiyi.opscloud.domain.notice.INoticeMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/4/19 11:24
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIamLoginProfileMessage implements INoticeMessage {

    private String awsName;
    private String loginUrl;
    @Schema(description = "账户ID")
    private String accountId;
    private String username;
    private String password;

}