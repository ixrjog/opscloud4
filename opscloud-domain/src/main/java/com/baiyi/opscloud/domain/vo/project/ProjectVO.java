package com.baiyi.opscloud.domain.vo.project;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.IBusinessPermissionUser;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2023/5/15 6:34 PM
 * @Since 1.0
 */
public class ProjectVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Project extends BaseVO implements
            IBusinessPermissionUser,
            BusinessDocumentVO.IBusinessDocument,
            UserVO.IUserPermission,
            TagVO.ITags {

        private final Integer businessType = BusinessTypeEnum.PROJECT.getType();

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private List<TagVO.Tag> tags;

        @Schema(description = "授权用户")
        private List<UserVO.User> users;

        private List<ProjectResourceVO.Resource> resources;

        private Map<String, List<ProjectResourceVO.Resource>> resourceMap;

        @Schema(description = "业务文档")
        private BusinessDocumentVO.Document document;

        @Schema(description = "部署总数")
        private Integer deployCount;

        @Schema(description = "环境部署次数")
        private Map<String, Integer> envDeployCount;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @NotNull(message = "项目名称不能为空")
        @Schema(description = "项目名称")
        private String name;

        @NotNull(message = "项目关键字不能为空")
        @Schema(description = "项目关键字")
        private String projectKey;

        @Schema(description = "项目类型")
        private String projectType;

        @Schema(description = "项目状态")
        private String projectStatus;

        @Schema(description = "开始时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date startTime;

        @Schema(description = "结束时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date endTime;

        @Schema(description = "描述")
        private String comment;

        // 应用授权角度
        private Integer userId;

        private Boolean isActive;

        private UserPermissionVO.UserPermission userPermission;

        private List<ProjectResourceVO.Resource> applicationList;

    }

}