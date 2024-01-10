package com.baiyi.opscloud.domain.notice.message;

import com.baiyi.opscloud.domain.notice.INoticeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/6/7 10:42
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRamLoginProfileMessage implements INoticeMessage {

    private String aliyunName;
    private String loginUrl;
    private String username;
    private String password;

}