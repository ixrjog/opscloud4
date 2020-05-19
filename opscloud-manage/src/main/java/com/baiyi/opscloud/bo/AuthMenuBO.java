package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/19 10:27 上午
 * @Version 1.0
 */
@Data
@Builder
public class AuthMenuBO {
    private Integer id;
    private Integer roleId;
    @Builder.Default
    private Integer menuType = 0;
    private String comment;
    @Builder.Default
    private String menu = "";
}
