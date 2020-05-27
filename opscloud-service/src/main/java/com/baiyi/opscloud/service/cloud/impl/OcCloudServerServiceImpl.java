package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;
import com.baiyi.opscloud.mapper.opscloud.OcCloudServerMapper;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
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
public class OcCloudServerServiceImpl implements OcCloudServerService {

    @Resource
    private OcCloudServerMapper ocCloudServerMapper;

    @Override
    public DataTable<OcCloudServer> queryOcCloudServerByParam(CloudServerParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcCloudServer> ocCloudServerList = ocCloudServerMapper.queryOcCloudServerByParam(pageQuery);
        return new DataTable<>(ocCloudServerList, page.getTotal());
    }

    @Override
    public List<OcCloudServer> queryOcCloudServerByType(int cloudServerType) {
        Example example = new Example(OcCloudServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudServerType", cloudServerType);
        return ocCloudServerMapper.selectByExample(example);
    }

    @Override
    public OcCloudServer queryOcCloudServerByInstanceId(String instanceId) {
        Example example = new Example(OcCloudServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocCloudServerMapper.selectOneByExample(example);
    }

    @Override
    public OcCloudServer queryOcCloudServerByUnqueKey(int cloudServerType, int serverId) {
        Example example = new Example(OcCloudServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cloudServerType", cloudServerType);
        criteria.andEqualTo("serverId", serverId);
        return ocCloudServerMapper.selectOneByExample(example);
    }

    @Override
    public OcCloudServer queryOcCloudServerById(int id) {
        return ocCloudServerMapper.selectByPrimaryKey(id);
    }


    @Override
    public void addOcCloudServer(OcCloudServer ocCloudserver) {
        ocCloudServerMapper.insert(ocCloudserver);
    }

    @Override
    public void updateOcCloudServer(OcCloudServer ocCloudserver) {
        ocCloudServerMapper.updateByPrimaryKey(ocCloudserver);
    }

    @Override
    public void deleteOcCloudServerById(int id) {
        ocCloudServerMapper.deleteByPrimaryKey(id);
    }
}
