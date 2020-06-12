package com.baiyi.opscloud.domain.vo.org;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/30 12:49 下午
 * @Version 1.0
 */
public class OrgApprovalVO {

    @Data
    @Builder
    @ApiModel
    public static class OrgApproval {

        @Builder.Default
        private Boolean isError = false;
        private String errorMsg;

        /**
         * 本人是否有审批权
         **/
        @Builder.Default
        private Boolean isApprovalAuthority = false;

        /**
         * 首选审批人
         **/
        private OrgDepartmentMemberVO.DepartmentMember preferenceDeptMember;

        /**
         * 备选审批人
         **/
        @Builder.Default
        private List<OrgDepartmentMemberVO.DepartmentMember> alternativeDeptMembers = Lists.newArrayList();
    }
}
