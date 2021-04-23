package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.common.base.ItAssetStatus;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAsset;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetMapper;
import com.baiyi.opscloud.service.it.OcItAssetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 2:41 下午
 * @Since 1.0
 */

@Service
public class OcItAssetServiceImpl implements OcItAssetService {

    @Resource
    private OcItAssetMapper ocItAssetMapper;

    @Override
    public OcItAsset queryOcItAssetById(Integer id) {
        return ocItAssetMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcItAsset(OcItAsset ocItAsset) {
        ocItAssetMapper.insert(ocItAsset);
    }

    @Override
    public void updateOcItAsset(OcItAsset ocItAsset) {
        ocItAssetMapper.updateByPrimaryKey(ocItAsset);
    }

    @Override
    public void deleteOcItAssetById(int id) {
        ocItAssetMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<OcItAsset> queryOcItAssetPage(ItAssetParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcItAsset> ocItAssetList = ocItAssetMapper.queryOcItAssetPage(pageQuery);
        return new DataTable<>(ocItAssetList, page.getTotal());
    }

    @Override
    public OcItAsset queryOcItAssetByAssetCode(String assetCode) {
        Example example = new Example(OcItAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetCode", assetCode);
        return ocItAssetMapper.selectOneByExample(example);
    }

    @Override
    public int countOcItAssetByStatus(Integer assetStatus) {
        Example example = new Example(OcItAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetStatus", assetStatus);
        return ocItAssetMapper.selectCountByExample(example);
    }

    @Override
    public int countOcItAsset() {
        Example example = new Example(OcItAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("assetStatus", ItAssetStatus.DISPOSE.getType());
        return ocItAssetMapper.selectCountByExample(example);
    }

    @Override
    public List<OcItAsset> queryOcItAssetAll() {
        return ocItAssetMapper.selectAll();
    }
}
