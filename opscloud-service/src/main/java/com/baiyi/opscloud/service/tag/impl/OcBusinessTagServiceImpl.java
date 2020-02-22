package com.baiyi.opscloud.service.tag.impl;

import com.baiyi.opscloud.domain.generator.OcBusinessTag;
import com.baiyi.opscloud.domain.vo.tag.OcBusinessTagVO;
import com.baiyi.opscloud.mapper.OcBusinessTagMapper;
import com.baiyi.opscloud.service.tag.OcBusinessTagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:08 下午
 * @Version 1.0
 */
@Service
public class OcBusinessTagServiceImpl implements OcBusinessTagService {

    @Resource
    private OcBusinessTagMapper ocBusinessTagMapper;

    @Override
    public OcBusinessTag queryOcBusinessTagByUniqueKey(OcBusinessTagVO.BusinessTag businessTag) {
        return ocBusinessTagMapper.queryOcBusinessTagByUniqueKey(businessTag);
    }

    @Override
    public void deleteOcBusinessTagByUniqueKey(OcBusinessTagVO.BusinessTag businessTag) {
        ocBusinessTagMapper.deleteOcBusinessTagByUniqueKey(businessTag);
    }

    @Override
    public void addOcBusinessTag(OcBusinessTag ocBusinessTag) {
        ocBusinessTagMapper.insert(ocBusinessTag);
    }

    @Override
    public void deleteOcBusinessTagById(int id) {
        ocBusinessTagMapper.deleteByPrimaryKey(id);
    }

}
