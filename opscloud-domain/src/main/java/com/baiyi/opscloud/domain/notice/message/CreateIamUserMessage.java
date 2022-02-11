package com.baiyi.opscloud.domain.notice.message;

import com.baiyi.opscloud.domain.notice.INoticeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/2/10 8:11 PM
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIamUserMessage implements INoticeMessage {

    private String awsName;
    private String loginUrl;
    private String accountId; // 账户ID
    private String username;
    private String password;

}
