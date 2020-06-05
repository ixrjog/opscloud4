package com.baiyi.opscloud.domain.vo.org;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:22 下午
 * @Version 1.0
 */
public class OrgDepartmentMemberVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DepartmentMember {

        private String displayName;
        private String email;

        private Integer id;
        private Integer departmentId;
        private Integer userId;
        private String username;
        private Integer memberType;
        private Integer isLeader;
        private Integer isApprovalAuthority;
        private Date createTime;
        private Date updateTime;
        private String comment;
    }
}
