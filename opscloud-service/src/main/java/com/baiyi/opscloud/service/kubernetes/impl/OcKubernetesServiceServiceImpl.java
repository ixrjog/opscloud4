package com.baiyi.opscloud.service.kubernetes.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesService;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesServiceParam;
import com.baiyi.opscloud.mapper.opscloud.OcKubernetesServiceMapper;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesServiceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 9:47 上午
 * @Version 1.0
 */
@Service
public class OcKubernetesServiceServiceImpl implements OcKubernetesServiceService {

    @Resource
    private OcKubernetesServiceMapper ocKubernetesServiceMapper;

    @Override
    public DataTable<OcKubernetesService> queryOcKubernetesServiceByParam(KubernetesServiceParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKubernetesService> list = ocKubernetesServiceMapper.queryOcKubernetesServiceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcKubernetesService> queryOcKubernetesServiceByNamespaceId(int namespaceId) {
        Example example = new Example(OcKubernetesService.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("namespaceId", namespaceId);
        return ocKubernetesServiceMapper.selectByExample(example);
    }

    @Override
    public OcKubernetesService queryOcKubernetesServiceByUniqueKey(int namespaceId, String name) {
        Example example = new Example(OcKubernetesService.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("namespaceId", namespaceId);
        criteria.andEqualTo("name", name);
        return ocKubernetesServiceMapper.selectOneByExample(example);
    }

    @Override
    public OcKubernetesService queryOcKubernetesServiceByInstanceId(int instanceId) {
        Example example = new Example(OcKubernetesService.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        PageHelper.startPage(1, 1);
        return ocKubernetesServiceMapper.selectOneByExample(example);
    }

    @Override
    public OcKubernetesService queryOcKubernetesServiceById(int id) {
        return ocKubernetesServiceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcKubernetesService(OcKubernetesService ocKubernetesService) {
        ocKubernetesServiceMapper.insert(ocKubernetesService);
    }

    @Override
    public void updateOcKubernetesService(OcKubernetesService ocKubernetesService) {
        ocKubernetesServiceMapper.updateByPrimaryKey(ocKubernetesService);
    }

    @Override
    public void deleteOcKubernetesServiceById(int id) {
        ocKubernetesServiceMapper.deleteByPrimaryKey(id);
    }

}
