package com.baiyi.opscloud.domain.vo.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 11:14 上午
 * @Version 1.0
 */
public class OcServerGroupVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroup {

        private OcServerGroupTypeVO.ServerGroupType serverGroupType;

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "组名称")
        private String name;

        @ApiModelProperty(value = "组类型",example="1")
        private Integer grpType;

        @ApiModelProperty(value = "是否支持工单", example = "1")
        private Integer inWorkorder;

        @ApiModelProperty(value = "资源描述")
        private String comment;

//        @ApiModelProperty(value = "创建时间")
//        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//        private Date createTime;
//
//        @ApiModelProperty(value = "更新时间")
//        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//        private Date updateTime;
    }

}
