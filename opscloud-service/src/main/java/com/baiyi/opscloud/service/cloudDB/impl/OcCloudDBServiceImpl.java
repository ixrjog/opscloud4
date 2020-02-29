package com.baiyi.opscloud.service.cloudDB.impl;

import com.baiyi.opscloud.domain.generator.OcCloudDb;
import com.baiyi.opscloud.mapper.OcCloudDbMapper;
import com.baiyi.opscloud.service.cloudDB.OcCloudDBService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/29 2:49 下午
 * @Version 1.0
 */
@Service
public class OcCloudDBServiceImpl implements OcCloudDBService {

    @Resource
    private OcCloudDbMapper ocCloudDbMapper ;

    @Override
    public OcCloudDb queryOcCloudDbByUniqueKey(int cloudDbType,String dbInstanceId) {
        Example example = new Example(OcCloudDb.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbType",cloudDbType);
        criteria.andEqualTo("dbInstanceId",dbInstanceId);
        return ocCloudDbMapper.selectOneByExample(example);
    }

    @Override
    public void addOcCloudDb(OcCloudDb ocCloudDb){
        ocCloudDbMapper.insert(ocCloudDb);
    }

    @Override
    public void updateOcCloudDb(OcCloudDb ocCloudDb){
        ocCloudDbMapper.updateByPrimaryKey(ocCloudDb);
    }
}
