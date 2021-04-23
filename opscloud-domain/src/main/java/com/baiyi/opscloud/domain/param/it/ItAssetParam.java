package com.baiyi.opscloud.domain.param.it;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 2:44 下午
 * @Since 1.0
 */
public class ItAssetParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "资产所属公司id")
        private Integer assetCompany;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        @ApiModelProperty(value = "资产状态")
        private Integer assetStatus;

        @ApiModelProperty(value = "资产名称id列表")
        private List<Integer> assetNameIdList;

        private Date useStartTime;

        private Date useEndTime;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApplyPageQuery extends PageParam {

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        @ApiModelProperty(value = "用户ID")
        private Integer userId;

        @ApiModelProperty(value = "用户所在一级部门id")
        private Integer userOrgDeptId;

        @ApiModelProperty(value = "申领方式")
        private Integer applyType;

        private Integer isReturn;

        private Date applyStartTime;

        private Date applyEndTime;

        private Date returnStartTime;

        private Date returnEndTime;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DisposePageQuery extends PageParam {

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        @ApiModelProperty(value = "处置方式")
        private Integer disposeType;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ApplyAsset {

        @ApiModelProperty(value = "资产ID")
        private Integer assetId;

        @ApiModelProperty(value = "用户ID")
        private Integer userId;

        @ApiModelProperty(value = "用户所在一级部门id")
        private Integer userOrgDeptId;

        @ApiModelProperty(value = "申领方式")
        private Integer applyType;

        @ApiModelProperty(value = "申领时间")
        private Long applyTime;

        @ApiModelProperty(value = "预计归还时间")
        private Long expectReturnTime;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ReturnAsset {

        private Integer id;

        @ApiModelProperty(value = "资产ID")
        private Integer assetId;

        @NotNull(message = "归还时间不能为空")
        @ApiModelProperty(value = "归还时间")
        private Long returnTime;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AddAssetName {

        @ApiModelProperty(value = "资产类型")
        private String assetType;

        @ApiModelProperty(value = "资产名称")
        private String assetName;

        @ApiModelProperty(value = "备注")
        private String remark;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DisposeAsset {

        @NotNull(message = "资产ID不能为空")
        @ApiModelProperty(value = "资产ID")
        private Integer assetId;

        /**
         * 1:退租
         * 2:报废清理
         * 3:盘亏处理
         * 4:转让出售
         */
        @NotNull(message = "处置类型不能为空")
        @ApiModelProperty(value = "处置类型")
        private Integer disposeType;

        private String expand;

        @ApiModelProperty(value = "备注")
        private String remark;

        @ApiModelProperty(value = "处置时间")
        private Long disposeTime;
    }

    @Data
    @Builder
    public static class ApplyAssetDingTalkMsg {

        private String displayName;

        private String assetName;
    }
}
