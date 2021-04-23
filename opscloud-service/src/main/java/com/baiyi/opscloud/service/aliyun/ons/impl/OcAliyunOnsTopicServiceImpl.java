package com.baiyi.opscloud.service.aliyun.ons.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunOnsTopicMapper;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsTopicService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 5:38 下午
 * @Since 1.0
 */

@Service
public class OcAliyunOnsTopicServiceImpl implements OcAliyunOnsTopicService {

    @Resource
    private OcAliyunOnsTopicMapper ocAliyunOnsTopicMapper;

    @Override
    public void addOcAliyunOnsTopic(OcAliyunOnsTopic ocAliyunOnsTopic) {
        ocAliyunOnsTopicMapper.insert(ocAliyunOnsTopic);
    }

    @Override
    public void updateOcAliyunOnsTopic(OcAliyunOnsTopic ocAliyunOnsTopic) {
        ocAliyunOnsTopicMapper.updateByPrimaryKey(ocAliyunOnsTopic);
    }

    @Override
    public void deleteOcAliyunOnsTopicById(int id) {
        ocAliyunOnsTopicMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcAliyunOnsTopic> queryOcAliyunOnsTopicByInstanceId(String instanceId) {
        Example example = new Example(OcAliyunOnsTopic.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocAliyunOnsTopicMapper.selectByExample(example);
    }

    @Override
    public DataTable<OcAliyunOnsTopic> queryOcAliyunOnsTopicByInstanceId(AliyunONSParam.TopicPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunOnsTopic> list = ocAliyunOnsTopicMapper.queryOcAliyunOnsTopicByInstanceId(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public OcAliyunOnsTopic queryOcAliyunOnsTopicByInstanceIdAndTopic(String instanceId, String topic) {
        Example example = new Example(OcAliyunOnsTopic.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        criteria.andEqualTo("topic", topic);
        return ocAliyunOnsTopicMapper.selectOneByExample(example);
    }

    @Override
    public int countOcAliyunOnsTopicByInstanceId(String instanceId) {
        Example example = new Example(OcAliyunOnsTopic.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return ocAliyunOnsTopicMapper.selectCountByExample(example);
    }

    @Override
    public int countOcAliyunOnsTopic() {
        Example example = new Example(OcAliyunOnsTopic.class);
        return ocAliyunOnsTopicMapper.selectCountByExample(example);
    }

    @Override
    public List<OcAliyunOnsTopic> queryOcAliyunOnsTopicByTopic(String topic) {
        Example example = new Example(OcAliyunOnsTopic.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("topic", topic);
        return ocAliyunOnsTopicMapper.selectByExample(example);
    }
}
