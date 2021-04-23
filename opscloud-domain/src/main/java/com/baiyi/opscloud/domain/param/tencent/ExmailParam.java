package com.baiyi.opscloud.domain.param.tencent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/29 2:27 下午
 * @Since 1.0
 */
public class ExmailParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class User {

        @ApiModelProperty(value = "成员UserID。企业邮帐号名，邮箱格式", example = "xiuyuan@xinc818.group")
        private String userid;
        @ApiModelProperty(value = "成员名称")
        private String name;
        @ApiModelProperty(value = "成员所属部门id列表")
        private List<Integer> department;
        @ApiModelProperty(value = "手机号")
        private String mobile;
        @ApiModelProperty(value = "启用/禁用成员。1表示启用成员，0表示禁用成员")
        private Integer enable;
    }
}
