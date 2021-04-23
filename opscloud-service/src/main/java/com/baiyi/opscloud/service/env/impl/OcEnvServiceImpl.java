package com.baiyi.opscloud.service.env.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.param.env.EnvParam;
import com.baiyi.opscloud.mapper.opscloud.OcEnvMapper;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 2:17 下午
 * @Version 1.0
 */
@Service
public class OcEnvServiceImpl implements OcEnvService {

    @Resource
    private OcEnvMapper ocEnvMapper;

    @Override
    public OcEnv queryOcEnvById(Integer id) {
        return ocEnvMapper.selectByPrimaryKey(id);
    }

    @Override
    public  OcEnv queryOcEnvByType(Integer type){
        Example example = new Example(OcEnv.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envType", type);
        return ocEnvMapper.selectOneByExample(example);
    }

    @Override
    public OcEnv queryOcEnvByName(String name) {
        Example example = new Example(OcEnv.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envName", name);
        return ocEnvMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcEnv> queryOcEnvByParam(EnvParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcEnv> ocEnvList = ocEnvMapper.queryOcEnvByParam(pageQuery);
        return new DataTable<>(ocEnvList, page.getTotal());
    }

    @Override
    public void addOcEnv(OcEnv ocEnv) {
        ocEnvMapper.insert(ocEnv);
    }

    @Override
    public void updateOcEnv(OcEnv ocEnv) {
        ocEnvMapper.updateByPrimaryKey(ocEnv);
    }

    @Override
    public void deleteOcEnvById(int id) {
        ocEnvMapper.deleteByPrimaryKey(id);
    }

}
