package com.baiyi.opscloud.domain.notice.message;

import com.baiyi.opscloud.domain.notice.INoticeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/12/14 11:04 AM
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRamUserMessage implements INoticeMessage {

    private String aliyunName;
    private String loginUrl;
    private String username;
    private String password;
    
}