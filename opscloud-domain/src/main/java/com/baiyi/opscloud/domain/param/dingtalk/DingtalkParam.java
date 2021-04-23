package com.baiyi.opscloud.domain.param.dingtalk;

import com.baiyi.opscloud.domain.param.account.AccountParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/14 1:46 下午
 * @Since 1.0
 */
public class DingtalkParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryByDeptId {

        private String uid;
        private String accessToken;
        @ApiModelProperty(value = "部门id")
        private Long deptId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserPageQuery extends AccountParam.AccountPageQuery {
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BindOcUser {

        private Integer accountId;
        private Integer ocUserId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UsernameConvert {

        private String displayName;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SendAsyncMsg {
        private String uid;
        private String accessToken;
        private Set<String> useridList;
        private DingtalkMsgParam msg;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GetUser {
        private String uid;
        private String userid;
    }
}
