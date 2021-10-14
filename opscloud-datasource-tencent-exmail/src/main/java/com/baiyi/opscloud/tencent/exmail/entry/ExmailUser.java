package com.baiyi.opscloud.tencent.exmail.entry;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/12 5:40 下午
 * @Since 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailUser {
    /**
     * 企业邮帐号名，邮箱格式
     */
    @JSONField(name = "userid")
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
