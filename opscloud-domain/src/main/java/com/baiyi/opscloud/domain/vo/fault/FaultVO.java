package com.baiyi.opscloud.domain.vo.fault;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 5:00 下午
 * @Since 1.0
 */
public class FaultVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class FaultInfo {

        private Integer id;

        @ApiModelProperty(value = "是否完成")
        private Boolean finalized;

        @ApiModelProperty(value = "故障标题")
        private String faultTitle;

        @ApiModelProperty(value = "故障级别")
        private String faultLevel;

        private String content;

        private Integer rootCauseTypeId;

        @ApiModelProperty(value = "原因归类")
        private String rootCauseType;

        @ApiModelProperty(value = "故障开始时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;

        @ApiModelProperty(value = "故障结束时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        private String ago;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

        @ApiModelProperty(value = "故障原因")
        private String rootCause;

        @ApiModelProperty(value = "故障详细过程")
        private String faultDetail;

        @ApiModelProperty(value = "故障评级原因")
        private String faultJudge;

        @ApiModelProperty(value = "所属团队")
        private String responsibleTeam;

        @ApiModelProperty(value = "造成的影响")
        private String effect;

        @ApiModelProperty(value = "现象故障产生的现象")
        private String faultPerformance;

        @ApiModelProperty(value = "主要责任人")
        private List<OcUser> primaryResponsiblePerson;

        @ApiModelProperty(value = "次要责任人")
        private List<OcUser> secondaryResponsiblePerson;

        @ApiModelProperty(value = "主要责任人用户id")
        private List<Integer> primaryResponsiblePersonIdList;

        @ApiModelProperty(value = "次要责任人用户id")
        private List<Integer> secondaryResponsiblePersonIdList;

        @ApiModelProperty(value = "解决方案")
        private List<FaultAction> todoAction;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class FaultAction {

        private Integer id;

        @ApiModelProperty(value = "故障id")
        private Integer faultId;

        @ApiModelProperty(value = "故障标题")
        private String faultTitle;

        @ApiModelProperty(value = "故障action")
        private String faultAction;

        @ApiModelProperty(value = "action跟进人Id")
        private Integer followUserId;

        @ApiModelProperty(value = "action跟进人")
        private OcUser followUser;

        /**
         * action状态
         * 0：完成
         * 1：待完成
         * 2：关闭
         */
        @ApiModelProperty(value = "action状态")
        private Integer actionStatus;

        @ApiModelProperty(value = "截止时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date deadline;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class InfoMonthStatsData {

        private String dateCat;
        private Integer p0;
        private Integer p1;
        private Integer p2;
        private Integer p3;
        private Integer p4;
        private Integer noFault;
        private Integer unrated;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class InfoMonthStats {

        private List<String> dateCat;
        private List<Integer> p0;
        private List<Integer> p1;
        private List<Integer> p2;
        private List<Integer> p3;
        private List<Integer> p4;
        private List<Integer> noFault;
        private List<Integer> unrated;
    }
}
