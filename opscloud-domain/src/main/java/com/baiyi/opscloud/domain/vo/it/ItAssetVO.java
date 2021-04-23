package com.baiyi.opscloud.domain.vo.it;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:26 下午
 * @Since 1.0
 */
public class ItAssetVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Asset {
        private Integer id;

        @ApiModelProperty(value = "资产所属公司id")
        private Integer assetCompany;

        @Excel(name = "资产所属公司", orderNum = "5")
        @ApiModelProperty(value = "资产所属公司")
        private String assetCompanyName;

        /**
         * 资产归属公司类型
         * 1:内部，所对应的购置方式为采购
         * 2:外部，所对应的购置方式为租赁
         */
        @ApiModelProperty(value = "资产归属公司类型")
        private Integer assetCompanyType;

        @Excel(name = "资产编码", orderNum = "1")
        @ApiModelProperty(value = "资产编码")
        private String assetCode;

        @Excel(name = "资产配置", orderNum = "9")
        @ApiModelProperty(value = "资产配置")
        private String assetConfiguration;

        @Excel(name = "购置金额", orderNum = "10")
        @ApiModelProperty(value = "购置金额")
        private String assetPrice;

        @Excel(name = "资产放置地点", orderNum = "11")
        @ApiModelProperty(value = "资产放置地点")
        private String assetPlace;

        private List<Integer> assetNameIds;

        @ApiModelProperty(value = "资产名称id")
        private Integer assetNameId;

        @Excel(name = "资产名称", orderNum = "4")
        @ApiModelProperty(value = "资产名称")
        private String assetName;

        @Excel(name = "资产分类", orderNum = "3")
        @ApiModelProperty(value = "资产分类")
        private String assetType;

        /**
         * 资产状态
         * 1:空闲
         * 2:在用
         * 3:借用
         * 4:处置
         */
        @Excel(name = "资产状态", replace = {"空闲_1", "在用_2", "借用_3", "处置_4"}, orderNum = "2")
        @ApiModelProperty(value = "资产状态")
        private Integer assetStatus;

        @ApiModelProperty(value = "处置类型")
        private Integer disposeType;

        @ApiModelProperty(value = "处置备注")
        private String disposeRemark;

        @ApiModelProperty(value = "领用/借用日期")
        @Excel(name = "领用/借用日期", orderNum = "7", format = "yyyy-MM-dd")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date useTime;

        private Long assetAddTimestamp;

        @ApiModelProperty(value = "购置/起租日期")
        @Excel(name = "购置/起租日期", orderNum = "6", format = "yyyy-MM-dd")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date assetAddTime;

        @Excel(name = "备注", orderNum = "12")
        @ApiModelProperty(value = "备注")
        private String remark;

        private OcUser user;

        @Excel(name = "领用人", orderNum = "8")
        private String displayName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetApply {
        private Integer id;

        @ApiModelProperty(name = "资产id")
        private Integer assetId;

        @Excel(name = "资产编码", orderNum = "1")
        @ApiModelProperty(name = "资产编码")
        private String assetCode;

        @ApiModelProperty(name = "申领用户id")
        private Integer userId;

        @ApiModelProperty(name = "申领用户名")
        private String username;

        @Excel(name = "领用人", orderNum = "2")
        @ApiModelProperty(name = "申领用户显示名称")
        private String displayName;

        @ApiModelProperty(name = "用户所在一级部门id")
        private Integer userOrgDeptId;

        @Excel(name = "用户所在部门", orderNum = "3")
        @ApiModelProperty(name = "用户所在一级部门名称")
        private String userOrgDeptName;

        /**
         * 申领方式
         * 1:使用
         * 2:借用
         */
        @Excel(name = "申领方式", replace = {"使用_1", "借用_2"}, orderNum = "4")
        @ApiModelProperty(name = "申领方式")
        private Integer applyType;

        @Excel(name = "领用/借用日期", orderNum = "5", format = "yyyy-MM-dd")
        @ApiModelProperty(name = "领用/借用日期")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date applyTime;

        @Excel(name = "预计归还时间", orderNum = "6", format = "yyyy-MM-dd")
        @ApiModelProperty(name = "预计归还时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date expectReturnTime;

        @Excel(name = "归还时间", orderNum = "7", format = "yyyy-MM-dd")
        @ApiModelProperty(name = "归还时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date returnTime;

        @ApiModelProperty(name = "是否归还")
        private Boolean isReturn;

        private Asset asset;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetDispose {
        private Integer id;

        @ApiModelProperty(name = "资产id")
        private Integer assetId;

        @ApiModelProperty(name = "资产编码")
        private String assetCode;


        @Column(name = "dispose_type")
        private Integer disposeType;

        private String expand;

        /**
         * 备注
         */
        private String remark;

        @ApiModelProperty(name = "处置日期")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date disposeTime;

        private Asset asset;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class StatsData {
        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "总计")
        private Integer value;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetStats {
        @ApiModelProperty(value = "资产名称统计")
        private List<StatsData> nameStatistics;

        @ApiModelProperty(value = "资产分类统计")
        private List<StatsData> typeStatistics;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetMonthStatsData {
        @ApiModelProperty(value = "日期")
        private String dateCat;

        @ApiModelProperty(value = "总计")
        private Integer value;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetMonthStats implements Serializable {
        private static final long serialVersionUID = 2383842671666791567L;
        @ApiModelProperty(value = "日期")
        private List<String> dateCatList;

        @ApiModelProperty(value = "资产名称月度统计")
        private Map<String, Map<String, List<Integer>>> nameStatistics;

        @ApiModelProperty(value = "资产分类月度统计")
        private Map<String, List<Integer>> typeStatistics;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetTotalStats {

        @ApiModelProperty(value = "资产总数")
        private Integer total;

        @ApiModelProperty(value = "空闲总数")
        private Integer freeTotal;

        @ApiModelProperty(value = "再用总数")
        private Integer usedTotal;

        @ApiModelProperty(value = "借用总数")
        private Integer borrowTotal;

        @ApiModelProperty(value = "处置总数")
        private Integer disposeTotal;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetCompanyTypeStats {

        @ApiModelProperty(value = "租赁总数")
        private Integer leaseTotal;

        @ApiModelProperty(value = "采购总数")
        private Integer purchaseTotal;

        @ApiModelProperty(value = "公司类型统计")
        private List<AssetCompanyStats> assetCompanyStats;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AssetCompanyStats {
        private Integer id;

        @ApiModelProperty(value = "租赁总数")
        private String name;

        @ApiModelProperty(value = "采购总数")
        private Integer value;

        @ApiModelProperty(value = "采购总数")
        private Integer type;

    }


}
