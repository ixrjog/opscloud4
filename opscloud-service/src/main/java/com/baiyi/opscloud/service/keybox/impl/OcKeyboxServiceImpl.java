package com.baiyi.opscloud.service.keybox.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKeybox;
import com.baiyi.opscloud.domain.param.keybox.KeyboxParam;
import com.baiyi.opscloud.mapper.opscloud.OcKeyboxMapper;
import com.baiyi.opscloud.service.keybox.OcKeyboxService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/3 10:30 上午
 * @Version 1.0
 */
@Service
public class OcKeyboxServiceImpl implements OcKeyboxService {

    @Resource
    private OcKeyboxMapper ocKeyboxMapper;

    @Override
    public OcKeybox queryOcKeyboxBySystemUser(String systemUser) {
        Example example = new Example(OcKeybox.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("systemUser", systemUser);
        return ocKeyboxMapper.selectOneByExample(example);
    }


    @Override
    public DataTable<OcKeybox> queryOcKeyboxByParam(KeyboxParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKeybox> list = ocKeyboxMapper.queryOcKeyboxByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public OcKeybox queryOcKeyboxById(int id) {
        return ocKeyboxMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateOcKeybox(OcKeybox ocKeybox) {
        ocKeyboxMapper.updateByPrimaryKey(ocKeybox);
    }

    @Override
    public void addOcKeybox(OcKeybox ocKeybox) {
        ocKeyboxMapper.insert(ocKeybox);
    }

    @Override
    public void deleteOcKeyboxById(int id) {
        ocKeyboxMapper.deleteByPrimaryKey(id);
    }

}
