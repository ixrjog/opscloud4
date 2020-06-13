package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/17 2:34 下午
 * @Version 1.0
 */
@Data
@Builder
public class UserSettingBO {

    private Integer id;
    private Integer userId;
    private String username;
    private String name;
    private String settingGroup;
    private String settingValue;
    @Builder.Default
    private Boolean encrypted = false;
    private String comment;

}
