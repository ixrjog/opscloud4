package com.baiyi.opscloud.domain.vo.business;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/8/2 12:39 下午
 * @Version 1.0
 */
public class BusinessAssetRelationVO {

    public interface IBusinessAssetRelation extends BaseBusiness.IBusiness {

        Integer getAssetId();

        void setAssetId(Integer assetId);

        /**
         * 业务对象唯一键
         * @return
         */
        String getBusinessUniqueKey();

        default Relation toBusinessAssetRelation() {
            return Relation.builder()
                    .businessType(this.getBusinessType())
                    .businessId(this.getBusinessId())
                    .datasourceInstanceAssetId(this.getAssetId())
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Relation extends BaseVO implements BaseBusiness.IBusiness {

        private Integer id;
        private Integer businessType;
        private Integer businessId;
        private Integer datasourceInstanceAssetId;
        private String assetType;

    }

}