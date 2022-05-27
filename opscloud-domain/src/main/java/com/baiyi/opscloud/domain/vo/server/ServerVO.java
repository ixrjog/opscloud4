package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:56 下午
 * @Version 1.0
 */
public class ServerVO {

    public interface IServer {
        void setServer(Server server);
        Integer getServerId();
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Server extends BaseVO implements EnvVO.IEnv,
            TagVO.ITags,
            ServerGroupVO.IServerGroup,
            ServerAccountVO.IAccount,
            BusinessAssetRelationVO.IBusinessAssetRelation, // 资产与业务对象绑定关系
            BusinessPropertyVO.IBusinessProperty,
            BusinessDocumentVO.IBusinessDocument,
            Serializable {

        @Override
        public String getBusinessUniqueKey() {
            return privateIp;
        }

        private static final long serialVersionUID = -1011261913967456450L;

        private BusinessDocumentVO.Document document;

        private List<TagVO.Tag> tags;

        private EnvVO.Env env;

        private ServerGroupVO.ServerGroup serverGroup;

        private final Integer businessType = BusinessTypeEnum.SERVER.getType();

        private List<ServerAccountVO.Account> accounts;

        private BusinessPropertyVO.Property businessProperty;

        @Override
        public Integer getServerId() {
            return id;
        }

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @ApiModelProperty(value = "主键", example = "1")
        @Builder.Default
        private Integer id = 0;

        @ApiModelProperty(value = "服务器名称")
        @NotBlank(message = "服务器名称不能为空")
        private String name;

        @ApiModelProperty(value = "显示名称")
        private String displayName;

        @ApiModelProperty(value = "服务器组id", example = "1")
        @NotNull(message = "服务器组不能为空")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型", example = "1")
        @NotNull(message = "环境类型不能为空")
        private Integer envType;

        @ApiModelProperty(value = "公网ip")
        private String publicIp;

        @ApiModelProperty(value = "私网ip")
        @NotNull(message = "私网ip不能为空")
        private String privateIp;

        @ApiModelProperty(value = "服务器类型", example = "1")
        @NotNull(message = "服务器类型不能为空")
        @Builder.Default
        private Integer serverType = 0;

        @ApiModelProperty(value = "地区")
        private String area;

        @ApiModelProperty(value = "系统类型")
        private String osType;

        @ApiModelProperty(value = "序号", example = "1")
        @Builder.Default
        private Integer serialNumber = 0;

        @ApiModelProperty(value = "监控状态", example = "1")
        private Integer monitorStatus;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        @ApiModelProperty(value = "服务器状态", example = "1")
        private Integer serverStatus;

        @ApiModelProperty(value = "有效")
        @Builder.Default
        private Boolean isActive = true;

        @ApiModelProperty(value = "资产id")
        private Integer assetId;

    }

}
