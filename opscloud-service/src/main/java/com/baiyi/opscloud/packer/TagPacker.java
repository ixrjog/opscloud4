package com.baiyi.opscloud.packer;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:41 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TagPacker {

    private final BusinessTagService businessTagService;

    public void wrap(TagVO.Tag tag){
        tag.setBusinessTypeEnum(BusinessTypeEnum.getByType(tag.getBusinessType()));
        tag.setQuantityUsed(businessTagService.countByTagId(tag.getId()));
    }

}