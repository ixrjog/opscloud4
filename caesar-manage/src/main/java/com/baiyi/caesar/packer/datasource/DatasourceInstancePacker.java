package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.packer.tag.TagPacker;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.datasource.DatasourceInstanceVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/19 5:20 下午
 * @Version 1.0
 */
@Component
public class DatasourceInstancePacker {

    @Resource
    private TagPacker tagPacker;

    public static DatasourceInstanceVO.Instance toVO(DatasourceInstance datasourceInstance){
        return BeanCopierUtil.copyProperties(datasourceInstance, DatasourceInstanceVO.Instance.class);
    }

    public void wrap(DatasourceInstanceVO.Instance instance){
        tagPacker.wrap(instance);
    }

    public List<DatasourceInstanceVO.Instance> wrapVOList(List<DatasourceInstance> data) {
        return BeanCopierUtil.copyListProperties(data, DatasourceInstanceVO.Instance.class);
    }

    public List<DatasourceInstanceVO.Instance> wrapVOList(List<DatasourceInstance> data, IExtend iExtend) {
        List<DatasourceInstanceVO.Instance> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;

        return voList.stream().peek(e ->
            wrap(e)
        ).collect(Collectors.toList());
    }
}