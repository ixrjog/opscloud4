package com.baiyi.caesar.domain.vo.datasource;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/11 10:57 上午
 * @Version 1.0
 */
public class DsAccountVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Account extends BaseVO {

        private Integer id;

        private String accountUid;


        private String accountId;


        private Integer accountType;


        private Integer userId;

        /**
         * 用户名
         */
        private String username;

        private String password;

        /**
         * 姓名
         */
        private String name;


        private String displayName;

        /**
         * 邮箱
         */
        private String email;


        private Boolean isActive;


        private Integer lastLogin;

        /**
         * 手机
         */
        private String phone;


        private String comment;

    }

}
