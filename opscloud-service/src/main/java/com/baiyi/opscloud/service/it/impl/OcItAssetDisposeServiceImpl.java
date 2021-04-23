package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetDispose;
import com.baiyi.opscloud.domain.param.it.ItAssetParam;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetDisposeMapper;
import com.baiyi.opscloud.service.it.OcItAssetDisposeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/30 4:19 下午
 * @Since 1.0
 */

@Service
public class OcItAssetDisposeServiceImpl implements OcItAssetDisposeService {

    @Resource
    private OcItAssetDisposeMapper ocItAssetDisposeMapper;

    @Override
    public void addOcItAssetDispose(OcItAssetDispose ocItAssetApply) {
        ocItAssetDisposeMapper.insert(ocItAssetApply);
    }

    @Override
    public void delOcItAssetDispose(Integer id) {
        ocItAssetDisposeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcItAssetDispose queryOcItAssetDisposeByAssetId(Integer assetId) {
        Example example = new Example(OcItAssetDispose.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return ocItAssetDisposeMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcItAssetDispose> queryOcItAssetDisposePage(ItAssetParam.DisposePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcItAssetDispose> ocItAssetDisposeList = ocItAssetDisposeMapper.queryOcItAssetDisposePage(pageQuery);
        return new DataTable<>(ocItAssetDisposeList, page.getTotal());
    }
}
