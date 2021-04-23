package com.baiyi.opscloud.service.kubernetes.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesClusterNamespace;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesClusterNamespaceParam;
import com.baiyi.opscloud.mapper.opscloud.OcKubernetesClusterNamespaceMapper;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterNamespaceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/28 1:54 下午
 * @Version 1.0
 */
@Service
public class OcKubernetesClusterNamespaceServiceImpl implements OcKubernetesClusterNamespaceService {

    @Resource
    private OcKubernetesClusterNamespaceMapper ocKubernetesClusterNamespaceMapper;

    @Override
    public DataTable<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByParam(KubernetesClusterNamespaceParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcKubernetesClusterNamespace> list = ocKubernetesClusterNamespaceMapper.queryOcKubernetesClusterNamespaceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcKubernetesClusterNamespace(OcKubernetesClusterNamespace ocKubernetesClusterNamespace) {
        ocKubernetesClusterNamespaceMapper.insert(ocKubernetesClusterNamespace);
    }

    @Override
    public void updateOcKubernetesClusterNamespace(OcKubernetesClusterNamespace ocKubernetesClusterNamespace) {
        ocKubernetesClusterNamespaceMapper.updateByPrimaryKey(ocKubernetesClusterNamespace);
    }

    @Override
    public void deleteOcKubernetesClusterNamespaceById(int id) {
        ocKubernetesClusterNamespaceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByClusterId(int clusterId) {
        Example example = new Example(OcKubernetesClusterNamespace.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("clusterId", clusterId);
        return ocKubernetesClusterNamespaceMapper.selectByExample(example);
    }

    @Override
    public OcKubernetesClusterNamespace queryOcKubernetesClusterNamespaceById(int id) {
        return ocKubernetesClusterNamespaceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcKubernetesClusterNamespace> queryOcKubernetesClusterNamespaceByEnvType(int envType) {
        Example example = new Example(OcKubernetesClusterNamespace.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envType", envType);
        return ocKubernetesClusterNamespaceMapper.selectByExample(example);
    }

    @Override
    public OcKubernetesClusterNamespace queryOcKubernetesClusterNamespaceByUniqueKey(int envType, String namespace) {
        Example example = new Example(OcKubernetesClusterNamespace.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envType", envType);
        criteria.andEqualTo("namespace", namespace);
        return ocKubernetesClusterNamespaceMapper.selectOneByExample(example);
    }
}
