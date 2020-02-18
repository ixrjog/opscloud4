package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.domain.param.cloudserver.CloudserverParam;
import com.baiyi.opscloud.mapper.OcCloudserverMapper;
import com.baiyi.opscloud.service.server.OcCloudserverService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:17 上午
 * @Version 1.0
 */
@Service
public class OcCloudserverServiceImpl implements OcCloudserverService {

    @Resource
    private OcCloudserverMapper ocCloudserverMapper;

    @Override
    public DataTable<OcCloudserver> queryOcCloudserverByParam(CloudserverParam.PageQuery pageQuery){
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudserver> ocCloudserverList =  ocCloudserverMapper.queryOcCloudserverByParam(pageQuery);
        return new DataTable<>(ocCloudserverList, page.getTotal());
    }

    @Override
    public List<OcCloudserver> queryOcCloudserverByType(int cloudserverType) {
        Example example = new Example(OcCloudserver.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudserverType",cloudserverType);
        return ocCloudserverMapper.selectByExample(example);
    }

    @Override
    public OcCloudserver queryOcCloudserverByInstanceId(String instanceId) {
        Example example = new Example(OcCloudserver.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId",instanceId);
        return ocCloudserverMapper.selectOneByExample(example);
    }


    @Override
    public  OcCloudserver queryOcCloudserverById(int id){
        return ocCloudserverMapper.selectByPrimaryKey(id);
    }


    @Override
    public void addOcCloudserver(OcCloudserver ocCloudserver) {
        ocCloudserverMapper.insert(ocCloudserver);
    }

    @Override
    public void updateOcCloudserver(OcCloudserver ocCloudserver) {
        ocCloudserverMapper.updateByPrimaryKey(ocCloudserver);
    }

    @Override
    public void deleteOcCloudserverById(int id) {
        ocCloudserverMapper.deleteByPrimaryKey(id);
    }
}
