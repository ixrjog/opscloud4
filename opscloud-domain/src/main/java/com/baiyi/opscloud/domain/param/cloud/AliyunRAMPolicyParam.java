package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/10 1:56 下午
 * @Version 1.0
 */
public class AliyunRAMPolicyParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "主账户uid")
        private String accountUid;

        @ApiModelProperty(value = "是否允许工单申请")
        private Integer inWorkorder;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserTicketOcRamPolicyQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        private String username;

        private List<Integer> ramUserIds;

        @ApiModelProperty(value = "主账户uid")
        private String accountUid;

        @ApiModelProperty(value = "工单票据id")
        private Integer workorderTicketId;

    }
}
