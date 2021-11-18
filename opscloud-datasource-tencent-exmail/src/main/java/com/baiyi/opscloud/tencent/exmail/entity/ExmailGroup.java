package com.baiyi.opscloud.tencent.exmail.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/13 10:38 上午
 * @Since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailGroup {

    @JSONField(name = "groupid")
    private String groupId;
    @JSONField(name = "groupname")
    private String groupName;
    @JSONField(name = "userlist")
    private List<String> userList;
    @JSONField(name = "grouplist")
    private List<String> groupList;
    private List<Long> department;
    @JSONField(name = "allow_type")
    private Integer allowType;
    @JSONField(name = "allow_userlist")
    private List<String> allowUserList;

}
