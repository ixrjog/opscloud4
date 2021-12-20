package com.baiyi.opscloud.tencent.exmail.entity;

import com.baiyi.opscloud.tencent.exmail.entity.base.BaseExmailResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/10/12 5:40 下午
 * @Since 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailUser extends BaseExmailResult {

    /**
     * 企业邮帐号名，邮箱格式
     */
    @JsonProperty("userid")
    private String userId;
    private String name;

    private List<Long> department;
    /**
     * 职位信息
     */
    private String position;
    private String mobile;
    private String tel;
    /**
     * 别名列表
     * 1、Slaves上限为5个
     * 2、Slaves为邮箱格式
     */
    private List<String> slaves;

    /**
     * 1 true
     * 0 false
     */
    private String enable;
}
