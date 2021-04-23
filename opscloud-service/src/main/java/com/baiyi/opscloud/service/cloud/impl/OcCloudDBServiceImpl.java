package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.param.cloud.CloudDBParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudDbMapper;
import com.baiyi.opscloud.service.cloud.OcCloudDBService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/29 2:49 下午
 * @Version 1.0
 */
@Service
public class OcCloudDBServiceImpl implements OcCloudDBService {

    @Resource
    private OcCloudDbMapper ocCloudDbMapper;

    @Override
    public DataTable<OcCloudDb> fuzzyQueryOcCloudDBByParam(CloudDBParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcCloudDb> ocCloudDbList = ocCloudDbMapper.fuzzyQueryOcCloudDbByParam(pageQuery);
        return new DataTable<>(ocCloudDbList, page.getTotal());
    }

    @Override
    public OcCloudDb queryOcCloudDbByUniqueKey(int cloudDbType, String dbInstanceId) {
        Example example = new Example(OcCloudDb.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbType", cloudDbType);
        criteria.andEqualTo("dbInstanceId", dbInstanceId);
        return ocCloudDbMapper.selectOneByExample(example);
    }

    @Override
    public void addOcCloudDb(OcCloudDb ocCloudDb) {
        ocCloudDbMapper.insert(ocCloudDb);
    }

    @Override
    public void updateOcCloudDb(OcCloudDb ocCloudDb) {
        ocCloudDbMapper.updateByPrimaryKey(ocCloudDb);
    }

    @Override
    public OcCloudDb queryOcCloudDbById(int id) {
        return ocCloudDbMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteOcCloudDbById(int id) {
        ocCloudDbMapper.deleteByPrimaryKey(id);
    }
}
