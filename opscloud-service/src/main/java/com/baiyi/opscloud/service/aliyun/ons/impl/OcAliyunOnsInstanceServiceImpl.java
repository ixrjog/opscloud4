package com.baiyi.opscloud.service.aliyun.ons.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunOnsInstanceMapper;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsInstanceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 2:17 下午
 * @Since 1.0
 */

@Service
public class OcAliyunOnsInstanceServiceImpl implements OcAliyunOnsInstanceService {

    @Resource
    private OcAliyunOnsInstanceMapper ocAliyunOnsInstanceMapper;

    @Override
    public void addOcAliyunOnsInstance(OcAliyunOnsInstance ocAliyunOnsInstance) {
        ocAliyunOnsInstanceMapper.insert(ocAliyunOnsInstance);
    }

    @Override
    public void updateOcAliyunOnsInstance(OcAliyunOnsInstance ocAliyunOnsInstance) {
        ocAliyunOnsInstanceMapper.updateByPrimaryKey(ocAliyunOnsInstance);
    }

    @Override
    public void deleteOcAliyunOnsInstanceById(int id) {
        ocAliyunOnsInstanceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceByRegionId(String regionId) {
        Example example = new Example(OcAliyunOnsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("regionId", regionId);
        return ocAliyunOnsInstanceMapper.selectByExample(example);
    }

    @Override
    public OcAliyunOnsInstance queryOcAliyunOnsInstanceByInstanceId(String instanceId) {
        Example example = new Example(OcAliyunOnsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocAliyunOnsInstanceMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceAll() {
        return ocAliyunOnsInstanceMapper.selectAll();
    }

    @Override
    public int countOcAliyunOnsInstance() {
        Example example = new Example(OcAliyunOnsInstance.class);
        return ocAliyunOnsInstanceMapper.selectCountByExample(example);
    }

    @Override
    public List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceByInstanceIdList(List<String> instanceIdList) {
        return ocAliyunOnsInstanceMapper.queryOcAliyunOnsInstanceByInstanceIdList(instanceIdList);
    }

    @Override
    public OcAliyunOnsInstance queryOcAliyunOnsInstance(int id) {
        return ocAliyunOnsInstanceMapper.selectByPrimaryKey(id);
    }
}
