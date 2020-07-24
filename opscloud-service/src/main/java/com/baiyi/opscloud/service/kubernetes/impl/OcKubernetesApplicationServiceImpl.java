package com.baiyi.opscloud.service.kubernetes.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplication;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesClusterNamespace;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesApplicationParam;
import com.baiyi.opscloud.mapper.opscloud.OcKubernetesApplicationMapper;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:13 下午
 * @Version 1.0
 */
@Service
public class OcKubernetesApplicationServiceImpl implements OcKubernetesApplicationService {

    @Resource
    private OcKubernetesApplicationMapper ocKubernetesApplicationMapper;

    @Override
    public DataTable<OcKubernetesApplication> queryOcKubernetesApplicationByParam(KubernetesApplicationParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKubernetesApplication> list = ocKubernetesApplicationMapper.queryOcKubernetesApplicationByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication) {
        ocKubernetesApplicationMapper.insert(ocKubernetesApplication);
    }

    @Override
    public void updateOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication) {
        ocKubernetesApplicationMapper.updateByPrimaryKey(ocKubernetesApplication);
    }

    @Override
    public void deleteOcKubernetesApplicationById(int id) {
        ocKubernetesApplicationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcKubernetesApplication queryOcKubernetesApplicationById(int id) {
        return ocKubernetesApplicationMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcKubernetesApplication queryOcKubernetesApplicationByName(String name) {
        Example example = new Example(OcKubernetesApplication.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return ocKubernetesApplicationMapper.selectOneByExample(example);
    }
}
