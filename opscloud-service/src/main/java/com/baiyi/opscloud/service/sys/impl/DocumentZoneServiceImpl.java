package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DocumentZone;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.mapper.DocumentZoneMapper;
import com.baiyi.opscloud.service.sys.DocumentZoneService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/2/8 10:12
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DocumentZoneServiceImpl implements DocumentZoneService {

    private final DocumentZoneMapper documentZoneMapper;

    @Override
    public DocumentZone getByMountZone(String mountZone) {
        Example example = new Example(DocumentZone.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mountZone", mountZone)
                .andEqualTo("isActive", true);
        return documentZoneMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<DocumentZone> queryPageByParam(DocumentParam.DocumentZonePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(DocumentZone.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getQueryName()));
        }
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("create_time");
        List<DocumentZone> data = documentZoneMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void updateByPrimaryKeySelective(DocumentZone documentZone) {
        documentZoneMapper.updateByPrimaryKeySelective(documentZone);
    }

}