package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbDatabase;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudDbDatabaseMapper;
import com.baiyi.opscloud.service.cloud.OcCloudDBDatabaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/1 6:52 下午
 * @Version 1.0
 */
@Service
public class OcCloudDBDatabaseServiceImpl implements OcCloudDBDatabaseService {

    @Resource
    private OcCloudDbDatabaseMapper ocCloudDbDatabaseMapper;

    @Override
    public OcCloudDbDatabase queryOcCloudDbDatabaseById(int id) {
        return ocCloudDbDatabaseMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcCloudDbDatabase queryOcCloudDbDatabaseByUniqueKey(int cloudDbId, String dbName) {
        Example example = new Example(OcCloudDbDatabase.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbId", cloudDbId);
        criteria.andEqualTo("dbName", dbName);
        return ocCloudDbDatabaseMapper.selectOneByExample(example);
    }

    @Override
    public void addOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase) {
        ocCloudDbDatabaseMapper.insert(ocCloudDbDatabase);
    }

    @Override
    public void updateOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase) {
        ocCloudDbDatabaseMapper.updateByPrimaryKey(ocCloudDbDatabase);
    }

    @Override
    public void delOcCloudDbDatabaseById(int id) {
        ocCloudDbDatabaseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcCloudDbDatabase> queryOcCloudDbDatabaseByCloudDbId(int cloudDbId) {
        Example example = new Example(OcCloudDbDatabase.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudDbId", cloudDbId);
        return ocCloudDbDatabaseMapper.selectByExample(example);
    }

    @Override
    public DataTable<OcCloudDbDatabase> fuzzyQueryOcCloudDBDatabaseByParam(CloudDBDatabaseParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcCloudDbDatabase> ocCloudDbDatabaseList = ocCloudDbDatabaseMapper.fuzzyQueryOcCloudDbDatabaseByParam(pageQuery);
        return new DataTable<>(ocCloudDbDatabaseList, page.getTotal());
    }

    @Override
    public void updateBaseOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase){
        ocCloudDbDatabaseMapper.updateBaseOcCloudDbDatabase(ocCloudDbDatabase);
    }


}
