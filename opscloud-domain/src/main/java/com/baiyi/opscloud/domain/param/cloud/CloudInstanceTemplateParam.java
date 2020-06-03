package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:39 下午
 * @Version 1.0
 */
public class CloudInstanceTemplateParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "云类型")
        private Integer cloudType;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CreateCloudInstance {

        @ApiModelProperty(value = "模版id")
        private Integer templateId;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;

        @ApiModelProperty(value = "服务器名称")
        private String serverName;

        @ApiModelProperty(value = "安全组id")
        private String securityGroupId;

        @ApiModelProperty(value = "镜像id")
        private String imageId;

        @ApiModelProperty(value = "登录账户")
        private String loginUser;
        @ApiModelProperty(value = "登录类型", example = "0")
        private Integer loginType;

        @ApiModelProperty(value = "环境类型", example = "0")
        private Integer envType;

        @ApiModelProperty(value = "分配公网ip")
        private Boolean allocatePublicIpAddress;

        @ApiModelProperty(value = "创建数量", example = "1")
        private Integer createSize;

        @ApiModelProperty(value = "可用区模式", example = "auto|single")
        private String zonePattern;

        @ApiModelProperty(value = "可用区id")
        private String zoneId;

        @ApiModelProperty(value = "选择虚拟交换机列表id")
        private List<String> vswitchIds;

        @ApiModelProperty(value = "付费选项")
        private Charge charge;

        @ApiModelProperty(value = "磁盘选项")
        private CloudInstanceTemplateVO.Disk disk;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Charge {
        @ApiModelProperty(value = "付费类型", example = "false:按量付费|true:预付费")
        private Boolean chargeType;

        @ApiModelProperty(value = "包月时长", example = "1,2,3,4,5,6,7,8,9,12,24,36")
        private Integer period;

        @ApiModelProperty(value = "自动续费")
        private Boolean autoRenew;
    }

}
