package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.IBusinessPermissionUser;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:11 下午
 * @Version 1.0
 */
public class ApplicationVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Kubernetes extends BaseVO implements IBusinessPermissionUser, BusinessDocumentVO.IBusinessDocument, UserVO.IUserPermission, TagVO.ITags {

        private final Integer businessType = BusinessTypeEnum.APPLICATION.getType();

        private ArmsTraceApp armsTraceApp;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private List<TagVO.Tag> tags;

        @Schema(description = "授权用户")
        private List<UserVO.User> users;

        private List<ApplicationResourceVO.Resource> resources;

        private Map<String, List<ApplicationResourceVO.Resource>> resourceMap;

        @Schema(description = "业务文档")
        private BusinessDocumentVO.Document document;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "应用名称")
        private String name;

        @Schema(description = "应用关键字")
        private String applicationKey;

        private Integer applicationType;

        @Schema(description = "描述")
        private String comment;

        // 应用授权角度
        private Integer userId;

        private Boolean isActive;

        private UserPermissionVO.UserPermission userPermission;

    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ArmsTraceApp {

        @Schema(description = "是否显示")
        private boolean show;

        private String homeUrl;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Application extends BaseVO implements IBusinessPermissionUser, BusinessDocumentVO.IBusinessDocument, UserVO.IUserPermission, TagVO.ITags {

        private final Integer businessType = BusinessTypeEnum.APPLICATION.getType();

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private List<TagVO.Tag> tags;

        @Schema(description = "授权用户")
        private List<UserVO.User> users;

        private List<ApplicationResourceVO.Resource> resources;

        private Map<String, List<ApplicationResourceVO.Resource>> resourceMap;

        @Schema(description = "Leo任务")
        private List<LeoJobVO.Job> leoJobs;

        @Schema(description = "业务文档")
        private BusinessDocumentVO.Document document;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "应用名称")
        private String name;

        @Schema(description = "应用关键字")
        private String applicationKey;

        private Integer applicationType;

        @Schema(description = "描述")
        private String comment;

        // 应用授权角度
        private Integer userId;

        private Boolean isActive;

        private UserPermissionVO.UserPermission userPermission;

    }

}