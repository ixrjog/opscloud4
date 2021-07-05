package com.baiyi.opscloud.domain.vo.datasource;

import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Asset extends BaseVO implements TagVO.ITags {

        // ITags
        private List<TagVO.Tag> tags;
        private final int businessType = BusinessTypeEnum.ASSET.getType();

        @Override
        public int getBusinessId() {
            return id;
        }

        private Map<String, String> properties;

        private Map<String, List<DsAssetVO.Asset>> children;

        private Map<String, List<DsAssetVO.Asset>> tree;

        private DsInstanceVO.Instance dsInstance;

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
