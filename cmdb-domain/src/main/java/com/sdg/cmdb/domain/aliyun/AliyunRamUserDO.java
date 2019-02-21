package com.sdg.cmdb.domain.aliyun;

import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunRamUserDO implements Serializable {

    public AliyunRamUserDO( ){
    }

    public AliyunRamUserDO( UserDO userDO){
        this.ramUserName = userDO.getUsername();
    }

    public AliyunRamUserDO(CreateUserResponse.User user){
        setRamUserName(user.getUserName());
        setRamDisplayName(user.getDisplayName());
        setRamUserId(user.getUserId());
        setMobilePhone(user.getMobilePhone());
        setEmail(user.getEmail());
        setComments(user.getComments());
    }

    public AliyunRamUserDO(ListUsersResponse.User user){
        setRamUserName(user.getUserName());
        setRamDisplayName(user.getDisplayName());
        setRamUserId(user.getUserId());
        setMobilePhone(user.getMobilePhone());
        setEmail(user.getEmail());
        setComments(user.getComments());
    }

    private static final long serialVersionUID = 6074840002543802659L;
    private long id;
    private String ramUserId;
    private String ramUserName;
    private String ramDisplayName;
    private String mobilePhone;
    private String email;
    private String comments;
    private int accessKeys;
    private String userTag;
    private long userId;
    private String gmtCreate;
    private String gmtModify;

}
