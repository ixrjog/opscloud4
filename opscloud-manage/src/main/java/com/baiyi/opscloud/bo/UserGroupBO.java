package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/2/26 10:07 上午
 * @Version 1.0
 */
@Data
@Builder
public class UserGroupBO {

    private Integer id;
    private String name;
    @Builder.Default
    private Integer grpType = 0;
    @Builder.Default
    private Integer workflow = 0;
    @Builder.Default
    private String source = "ldap";
    private String comment;

}
