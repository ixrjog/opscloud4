package com.baiyi.opscloud.domain.vo.business;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 4:32 下午
 * @Since 1.0
 */
public class BusinessRelationVO {

    public interface IRelation extends BaseBusiness.IBusiness {

        void setSourceBusinessRelations(List<BusinessRelationVO.Relation> sourceBusinessRelations);

        List<BusinessRelationVO.Relation> getSourceBusinessRelations();

        void setTargetBusinessRelations(List<BusinessRelationVO.Relation> targetBusinessRelations);

        List<BusinessRelationVO.Relation> getTargetBusinessRelations();

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Relation extends BaseVO implements Serializable {

        private static final long serialVersionUID = -7796721327490285102L;
        private Integer id;

        private Integer sourceBusinessType;

        private Integer sourceBusinessId;

        private Integer targetBusinessType;

        private Integer targetBusinessId;

        private String relationType;
    }
}
