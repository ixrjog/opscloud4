package com.baiyi.opscloud.ldap.entry;

import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2019/12/27 1:04 下午
 * @Version 1.0
 */
@Data
@ToString
@Entry(objectClasses = {"inetorgperson"}, base = "ou=users")
public class Person {
    /**
     * 主键
     */
    @Attribute
    private String personId;

    /**
     * 用户名
     */
    @Attribute(name = "cn")
    private String username;

    /**
     * 显示名
     */
    @Attribute(name = "displayName")
    private String displayName;

    /**
     * 电话
     */
    @Attribute(name = "mobile")
    private String mobile;
    /**
     * 邮箱
     */
    @Attribute(name = "mail")
    private String email;

    /**
     * 工号
     */
    @Attribute(name = "jobNo")
    private String jobNo;

    /**
     * 证件类型
     */
    @Attribute(name = "certType")
    private Integer certType;
    /**
     * 证件号码
     */
    @Attribute(name = "certificateNo")
    private String certNo;

    @Attribute(name = "userPassword")
    private String userPassword;

    /**
     * 有效
     */
    @Attribute(name = "accountStatus")
    private Boolean isActive;


    @Attribute
    protected Date createTime;

    /**
     * 更新时间
     */
    @Attribute
    protected Date updateTime;

    /**
     * 状态
     */
    @Attribute
    protected Integer status;

    @Attribute
    protected Integer disOrder;

    /**
     * 工作单位
     */
    @Attribute
    private String company;
}

