package com.baiyi.opscloud.ldap.entry;

import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

/**
 * @Author baiyi
 * @Date 2019/12/27 5:49 下午
 * @Version 1.0
 */
@Data
@ToString
@Entry(objectClasses = {"groupOfUniqueNames"}, base = "ou=groups")
public class Group {

    /**
     * 主键
     */
    @Attribute
    private String groupId;

    /**
     * 组名
     */
    @Attribute(name = "cn")
    private String groupName;
}
