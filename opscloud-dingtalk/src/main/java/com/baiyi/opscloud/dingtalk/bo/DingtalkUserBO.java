package com.baiyi.opscloud.dingtalk.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 5:57 下午
 * @Since 1.0
 */
@Data
public class DingtalkUserBO implements Serializable {
    private static final long serialVersionUID = -1650045343491860651L;
    private String uid;
    private Boolean active;
    private Boolean admin;
    private String avatar;
    private Boolean boss;
    private List<Long> deptIdList;
    private Long deptOrder;
    private String email;
    private Boolean exclusiveAccount;
    private String extension;
    private Boolean hideMobile;
    private Long hiredDate;
    private String jobNumber;
    private Boolean leader;
    private String mobile;
    private String name;
    private String orgEmail;
    private String remark;
    private String stateCode;
    private String telephone;
    private String title;
    private String unionid;
    private String userid;
    private String workPlace;
}
