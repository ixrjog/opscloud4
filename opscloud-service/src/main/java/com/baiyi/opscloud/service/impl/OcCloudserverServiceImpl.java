package com.baiyi.opscloud.service.impl;

import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.mapper.OcCloudserverMapper;
import com.baiyi.opscloud.service.OcCloudserverService;
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
    public  OcCloudserver queryOcCloudserver(int id){
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
    public void delOcCloudserver(int id) {
        ocCloudserverMapper.deleteByPrimaryKey(id);
    }
}
