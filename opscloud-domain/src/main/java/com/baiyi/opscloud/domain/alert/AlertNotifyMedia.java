package com.baiyi.opscloud.domain.alert;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/21 3:29 PM
 * @Since 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertNotifyMedia {

    private List<User> users;

    private String dingtalkToken;

    private String ttsCode;

    private String templateCode;

}