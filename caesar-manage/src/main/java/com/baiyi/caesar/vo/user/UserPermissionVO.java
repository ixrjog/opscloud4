package com.baiyi.caesar.vo.user;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/8/14 1:58 下午
 * @Version 1.0
 */
public class UserPermissionVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserPermission {

        private Integer id;
        private Integer userId;
        private Integer businessId;
        private Integer businessType;
        private Integer rate;
        private String roleName;
        private String content;

    }


}
