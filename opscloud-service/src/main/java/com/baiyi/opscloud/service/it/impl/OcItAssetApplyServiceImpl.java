package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetApply;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetApplyMapper;
import com.baiyi.opscloud.service.it.OcItAssetApplyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:09 下午
 * @Since 1.0
 */

@Service
public class OcItAssetApplyServiceImpl implements OcItAssetApplyService {

    @Resource
    private OcItAssetApplyMapper ocItAssetApplyMapper;

    @Override
    public OcItAssetApply queryOcItAssetApplyById(Integer id) {
        return ocItAssetApplyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcItAssetApply(OcItAssetApply ocItAssetApply) {
        ocItAssetApplyMapper.insert(ocItAssetApply);
    }

    @Override
    public void updateOcItAssetApply(OcItAssetApply ocItAssetApply) {
        ocItAssetApplyMapper.updateByPrimaryKey(ocItAssetApply);
    }

    @Override
    public DataTable<OcItAssetApply> queryOcItAssetApplyPage(ItAssetParam.ApplyPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcItAssetApply> ocItAssetList = ocItAssetApplyMapper.queryOcItAssetApplyPage(pageQuery);
        return new DataTable<>(ocItAssetList, page.getTotal());
    }

    @Override
    public OcItAssetApply queryOcItAssetApplyByAssetId(Integer assetId) {
        Example example = new Example(OcItAssetApply.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        criteria.andEqualTo("isReturn", false);
        return ocItAssetApplyMapper.selectOneByExample(example);
    }

    @Override
    public List<OcItAssetApply> queryMyAsset(Integer userId) {
        Example example = new Example(OcItAssetApply.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("isReturn", false);
        return ocItAssetApplyMapper.selectByExample(example);
    }

    @Override
    public List<OcItAssetApply> queryOcItAssetApplyAll() {
        return ocItAssetApplyMapper.selectAll();
    }
}
