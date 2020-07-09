package com.baiyi.opscloud.service.kubernetes.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesDeployment;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesDeploymentParam;
import com.baiyi.opscloud.mapper.opscloud.OcKubernetesDeploymentMapper;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesDeploymentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/29 2:35 下午
 * @Version 1.0
 */
@Service
public class OcKubernetesDeploymentServiceImpl implements OcKubernetesDeploymentService {

    @Resource
    private OcKubernetesDeploymentMapper ocKubernetesDeploymentMapper;

    @Override
    public DataTable<OcKubernetesDeployment> queryOcKubernetesDeploymentByParam(KubernetesDeploymentParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKubernetesDeployment> list = ocKubernetesDeploymentMapper.queryOcKubernetesDeploymentByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public OcKubernetesDeployment queryOcKubernetesDeploymentByInstanceId(int instanceId) {
        Example example = new Example(OcKubernetesDeployment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        PageHelper.startPage(1, 1);
        return ocKubernetesDeploymentMapper.selectOneByExample(example);
    }

    @Override
    public OcKubernetesDeployment queryOcKubernetesDeploymentById(int id) {
        return ocKubernetesDeploymentMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcKubernetesDeployment queryOcKubernetesDeploymentByUniqueKey(int namespaceId, String name) {
        Example example = new Example(OcKubernetesDeployment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("namespaceId", namespaceId);
        criteria.andEqualTo("name", name);
        return ocKubernetesDeploymentMapper.selectOneByExample(example);
    }

    @Override
    public void addOcKubernetesDeployment(OcKubernetesDeployment ocKubernetesDeployment) {
        ocKubernetesDeploymentMapper.insert(ocKubernetesDeployment);
    }

    @Override
    public void updateOcKubernetesDeployment(OcKubernetesDeployment ocKubernetesDeployment) {
        ocKubernetesDeploymentMapper.updateByPrimaryKey(ocKubernetesDeployment);
    }

    @Override
    public void deleteOcKubernetesDeploymentById(int id) {
        ocKubernetesDeploymentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcKubernetesDeployment> queryOcKubernetesDeploymentByNamespaceId(int namespaceId) {
        Example example = new Example(OcKubernetesDeployment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("namespaceId", namespaceId);
        return ocKubernetesDeploymentMapper.selectByExample(example);
    }

}
