package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.generator.OcCloudImage;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.mapper.opscloud.OcCloudImageMapper;
import com.baiyi.opscloud.service.cloud.OcCloudImageService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 9:14 上午
 * @Version 1.0
 */
@Service
public class OcCloudImageServiceImpl implements OcCloudImageService {

    @Resource
    private OcCloudImageMapper ocCloudImageMapper;

    @Override
    public List<OcCloudImage> queryOcCloudImageByType(int cloudType) {
        Example example = new Example(OcCloudImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudType",cloudType);
        return ocCloudImageMapper.selectByExample(example);
    }
}
