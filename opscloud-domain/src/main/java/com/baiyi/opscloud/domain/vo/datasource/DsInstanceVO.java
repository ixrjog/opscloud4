package com.baiyi.opscloud.domain.vo.datasource;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:54 下午
 * @Version 1.0
 */
public class DsInstanceVO {

    public interface IInstance {

        String getInstanceUuid();
    }

    public interface IDsInstance extends IInstance {

        void setInstance(Instance instance);

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Schema
    public static class Instance extends BaseVO implements TagVO.ITags {

        private final Integer businessType = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

        private List<TagVO.Tag> tags;

        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        private DsConfigVO.DsConfig dsConfig;

        private List<AssetDetail> assetDetails;

        @Schema(description = "任务数量", example = "1")
        private Integer jobSize;

        private List<Instance> children;
        private Integer id;
        private Integer parentId;
        private String instanceName;
        private String uuid;
        private String instanceType;
        private String kind;
        private String version;
        private Boolean isActive;
        private Integer configId;
        private Date createTime;
        private Date updateTime;
        private String comment;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AssetDetail {

        private String assetType;
        private Integer assetSize;

    }

}