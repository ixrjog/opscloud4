package com.baiyi.opscloud.domain.vo.auth;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:02 上午
 * @Version 1.0
 */
public class AuthMenuVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Menu {

        private String roleName;

        private Integer id;
        private Integer roleId;
        private Integer menuType;
        private String comment;
        private Date createTime;
        private Date updateTime;
        private String menu;

    }
}
