package com.baiyi.opscloud.service.aliyun.ons.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunOnsGroupMapper;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 8:17 下午
 * @Since 1.0
 */

@Service
public class OcAliyunOnsGroupServiceImpl implements OcAliyunOnsGroupService {

    @Resource
    private OcAliyunOnsGroupMapper ocAliyunOnsGroupMapper;

    @Override
    public OcAliyunOnsGroup queryOcAliyunOnsGroupById(Integer id) {
        return ocAliyunOnsGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunOnsGroup(OcAliyunOnsGroup ocAliyunOnsGroup) {
        ocAliyunOnsGroupMapper.insert(ocAliyunOnsGroup);
    }

    @Override
    public void updateOcAliyunOnsGroup(OcAliyunOnsGroup ocAliyunOnsGroup) {
        ocAliyunOnsGroupMapper.updateByPrimaryKey(ocAliyunOnsGroup);
    }

    @Override
    public void deleteOcAliyunOnsGroupById(int id) {
        ocAliyunOnsGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<OcAliyunOnsGroup> queryONSGroupPage(AliyunONSParam.GroupPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunOnsGroup> list = ocAliyunOnsGroupMapper.queryONSGroupPage(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcAliyunOnsGroup> queryOcAliyunOnsGroupByInstanceId(String instanceId) {
        Example example = new Example(OcAliyunOnsGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocAliyunOnsGroupMapper.selectByExample(example);
    }

    @Override
    public OcAliyunOnsGroup queryOcAliyunOnsGroupByInstanceIdAndGroupId(String instanceId, String groupId) {
        Example example = new Example(OcAliyunOnsGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        criteria.andEqualTo("groupId", groupId);
        return ocAliyunOnsGroupMapper.selectOneByExample(example);
    }

    @Override
    public int countOcAliyunOnsGroupByInstanceId(String instanceId) {
        Example example = new Example(OcAliyunOnsGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocAliyunOnsGroupMapper.selectCountByExample(example);
    }

    @Override
    public int countOcAliyunOnsGroup() {
        Example example = new Example(OcAliyunOnsGroup.class);
        return ocAliyunOnsGroupMapper.selectCountByExample(example);
    }

    @Override
    public List<OcAliyunOnsGroup> queryOcAliyunOnsGroupByGroupId(String groupId) {
        Example example = new Example(OcAliyunOnsGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return ocAliyunOnsGroupMapper.selectByExample(example);
    }
}
