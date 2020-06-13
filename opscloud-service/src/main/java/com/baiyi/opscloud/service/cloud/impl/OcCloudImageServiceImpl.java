package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;
import com.baiyi.opscloud.domain.param.cloud.CloudImageParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudImageMapper;
import com.baiyi.opscloud.service.cloud.OcCloudImageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
        criteria.andEqualTo("cloudType", cloudType);
        return ocCloudImageMapper.selectByExample(example);
    }

    @Override
    public void addOcCloudImage(OcCloudImage ocCloudImage) {
        ocCloudImageMapper.insert(ocCloudImage);
    }

    @Override
    public void updateOcCloudImage(OcCloudImage ocCloudImage) {
        ocCloudImageMapper.updateByPrimaryKey(ocCloudImage);
    }

    @Override
    public DataTable<OcCloudImage> fuzzyQueryOcCloudImageByParam(CloudImageParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudImage> ocCloudImageList = ocCloudImageMapper.fuzzyQueryOcCloudImageByParam(pageQuery);
        return new DataTable<>(ocCloudImageList, page.getTotal());
    }

    @Override
    public OcCloudImage queryOcCloudImageById(int id) {
        return ocCloudImageMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcCloudImage queryOcCloudImageByImageId(String imageId) {
        Example example = new Example(OcCloudImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("imageId", imageId);
        return ocCloudImageMapper.selectOneByExample(example);
    }

    @Override
    public void deleteOcCloudImageById(int id) {
        ocCloudImageMapper.deleteByPrimaryKey(id);
    }
}
