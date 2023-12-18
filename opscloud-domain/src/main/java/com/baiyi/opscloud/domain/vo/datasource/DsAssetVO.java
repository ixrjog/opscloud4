package com.baiyi.opscloud.domain.vo.datasource;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.business.BusinessRelationVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:25 下午
 * @Version 1.0
 */
public class DsAssetVO {

    public interface IDsAsset {

        /**
         * 资产ID
         * @return
         */
        Integer getAssetId();

        /**
         * 设置资产
         * @param asset
         */
        void setAsset(Asset asset);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Asset extends BaseVO implements TagVO.ITags, BusinessRelationVO.IRelations {

        private List<TagVO.Tag> tags;
        private final Integer businessType = BusinessTypeEnum.ASSET.getType();

        @Schema(description = "此资产可转换为其它业务对象")
        private Map<BusinessTypeEnum, BusinessAssetRelationVO.IBusinessAssetRelation> convertBusinessTypes;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "资产属性")
        private Map<String, String> properties;

        @Schema(description = "关系对象(以实体资产存在)")
        private Map<String, List<DsAssetVO.Asset>> children;

        @Schema(description = "子对象(依赖夫对象存在)")
        private Map<String, List<DsAssetVO.Asset>> tree;

        private DsLoginAssetVO.LoginAsset loginAsset;

        private DsInstanceVO.Instance dsInstance;

        private List<BusinessRelationVO.Relation> sourceBusinessRelations;

        private List<BusinessRelationVO.Relation> targetBusinessRelations;

        private Integer id;
        private Integer parentId;
        private String instanceUuid;
        private String name;
        private String assetId;
        private String assetType;
        private String kind;
        private String version;
        private Boolean isActive;
        private String assetKey;
        private String assetKey2;
        private String zone;
        private String regionId;
        private String assetStatus;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;
        private String description;

    }

}