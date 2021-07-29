package com.baiyi.opscloud.packer.business;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;
import com.baiyi.opscloud.domain.vo.business.BusinessRelationVO;
import com.baiyi.opscloud.service.business.BusinessRelationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 5:13 下午
 * @Since 1.0
 */

@Component
public class BusinessRelationPacker {

    @Resource
    private BusinessRelationService businessRelationService;

    public void wrap(BusinessRelationVO.IRelation iRelation) {
        List<BusinessRelation> sourceRelations = businessRelationService.listBySourceBusiness(iRelation.getBusinessType(), iRelation.getBusinessId());
        iRelation.setSourceBusinessRelations(wrapVOList(sourceRelations));
        List<BusinessRelation> targetRelations = businessRelationService.listByTargetBusiness(iRelation.getBusinessType(), iRelation.getBusinessId());
        iRelation.setTargetBusinessRelations(wrapVOList(targetRelations));
    }

    public List<BusinessRelationVO.Relation> wrapVOList(List<BusinessRelation> relations) {
        return BeanCopierUtil.copyListProperties(relations, BusinessRelationVO.Relation.class);
    }
}
