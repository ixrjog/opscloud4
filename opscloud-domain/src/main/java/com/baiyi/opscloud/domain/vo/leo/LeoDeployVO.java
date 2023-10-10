package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.baiyi.opscloud.domain.vo.common.IHeartbeat;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/6 19:56
 * @Version 1.0
 */
public class LeoDeployVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Deploy implements Serializable, ReadableTime.IAgo, ReadableTime.IRuntime, TagVO.ITags, IHeartbeat {

        @Serial
        private static final long serialVersionUID = -6080138223431460692L;

        // ITags
        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        private final Integer businessType = BusinessTypeEnum.LEO_DEPLOY.getType();

        private List<TagVO.Tag> tags;

        @Schema(description = "存活")
        private Boolean isLive;

        @Schema(description = "以前")
        private String ago;

        @Schema(description = "运行时长")
        private String runtime;

        @Schema(description = "部署详情")
        private Object deployDetails;

        @Schema(description = "部署日志")
        private List<LeoLogVO.Log> deployLogs;

        private Integer id;
        private Integer applicationId;
        private Integer jobId;
        private String jobName;
        private Integer deployNumber;
        private Integer buildId;
        private String versionName;
        private String versionDesc;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;
        private String deployStatus;
        private String deployResult;
        private Boolean isFinish;
        private Integer executionType;
        private String username;
        private Boolean isActive;
        private Boolean isRollback;
        private Date createTime;
        private Date updateTime;
        private String deployConfig;
        private String ocInstance;
        private String comment;

        @Override
        public Date getAgoTime() {
            return this.startTime != null ? this.createTime : new Date();
        }

    }

}