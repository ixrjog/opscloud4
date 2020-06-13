package com.baiyi.opscloud.domain.vo.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTaskMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/31 11:02 上午
 * @Version 1.0
 */
public class CloudInstanceTaskVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudInstanceTask  {

        private Map<String, List<OcCloudInstanceTaskMember>> memberMap;

        private Integer id;
        private Integer cloudType;
        private Integer templateId;
        private Integer userId;
        private String regionId;
        private Integer createSize;
        private String taskPhase;
        @ApiModelProperty(value = "完成百分比",example = "100")
        private Integer completedPercentage;
        private String taskStatus;
        private String errorMsg;
        private Integer taskType;
        private Date createTime;
        private Date updateTime;
        private String userDetail;
        private String createInstance;
        private String comment;

    }


}
