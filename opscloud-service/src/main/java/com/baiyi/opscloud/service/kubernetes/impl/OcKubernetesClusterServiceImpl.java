package com.baiyi.opscloud.service.kubernetes.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.domain.param.kubernetes.KubernetesClusterParam;
import com.baiyi.opscloud.mapper.opscloud.OcKubernetesClusterMapper;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesClusterService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/24 4:23 下午
 * @Version 1.0
 */
@Service
public class OcKubernetesClusterServiceImpl implements OcKubernetesClusterService {

    @Resource
    private OcKubernetesClusterMapper ocKubernetesClusterMapper;

    @Override
    public List<OcKubernetesCluster> queryAll() {
        return ocKubernetesClusterMapper.selectAll();
    }

    @Override
    public OcKubernetesCluster queryOcKubernetesClusterById(int id) {
        return ocKubernetesClusterMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcKubernetesCluster queryOcKubernetesClusterByName(String name) {
        Example example = new Example(OcKubernetesCluster.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return ocKubernetesClusterMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcKubernetesCluster> queryOcKubernetesClusterByParam(KubernetesClusterParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKubernetesCluster> list = ocKubernetesClusterMapper.queryOcKubernetesClusterByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcKubernetesCluster(OcKubernetesCluster ocKubernetesCluster) {
        ocKubernetesClusterMapper.insert(ocKubernetesCluster);
    }

    @Override
    public void updateOcKubernetesCluster(OcKubernetesCluster ocKubernetesCluster) {
        ocKubernetesClusterMapper.updateByPrimaryKey(ocKubernetesCluster);
    }

    @Override
    public void deleteOcKubernetesClusterById(int id) {
        ocKubernetesClusterMapper.deleteByPrimaryKey(id);
    }
}
