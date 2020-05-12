package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/21 10:30 下午
 * @Version 1.0
 */
@Builder
@Data
public class OrgDepartmentMemberBO {

    private Integer id;
    private Integer departmentId;
    private Integer userId;
    private String username;
    @Builder.Default
    private Integer memberType = 0;
    @Builder.Default
    private Integer isLeader = 0;
    @Builder.Default
    private Integer isApprovalAuthority = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
