package com.baiyi.opscloud.tencent.exmail.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/10/13 10:38 上午
 * @Since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExmailGroup {

    @JsonProperty("groupid")
    private String groupId;
    @JsonProperty("groupname")
    private String groupName;
    @JsonProperty("userlist")
    private List<String> userList;
    @JsonProperty("grouplist")
    private List<String> groupList;
    private List<Long> department;
    @JsonProperty("allow_type")
    private Integer allowType;
    @JsonProperty("allow_userlist")
    private List<String> allowUserList;

}