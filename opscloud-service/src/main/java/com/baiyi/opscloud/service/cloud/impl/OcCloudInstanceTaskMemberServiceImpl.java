package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTaskMember;
import com.baiyi.opscloud.mapper.opscloud.OcCloudInstanceTaskMemberMapper;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskMemberService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/30 4:37 下午
 * @Version 1.0
 */
@Service
public class OcCloudInstanceTaskMemberServiceImpl implements OcCloudInstanceTaskMemberService {
    @Resource
    private OcCloudInstanceTaskMemberMapper ocCloudInstanceTaskMemberMapper;

    @Override
    public List<OcCloudInstanceTaskMember> queryOcCloudInstanceTaskMemberByTaskId(int taskId) {
        Example example = new Example(OcCloudInstanceTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        return ocCloudInstanceTaskMemberMapper.selectByExample(example);
    }

    @Override
    public List<OcCloudInstanceTaskMember> queryOcCloudInstanceTaskMemberByTaskIdAndPhase(int taskId, String taskPhase) {
        Example example = new Example(OcCloudInstanceTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        criteria.andEqualTo("taskPhase", taskPhase);
        return ocCloudInstanceTaskMemberMapper.selectByExample(example);
    }

    @Override
    public int countOcCloudInstanceTaskMemberByTaskId(int taskId) {
        Example example = new Example(OcCloudInstanceTaskMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        return ocCloudInstanceTaskMemberMapper.selectCountByExample(example);
    }

    @Override
    public void addOcCloudInstanceTaskMember(OcCloudInstanceTaskMember ocCloudInstanceTaskMember) {
        ocCloudInstanceTaskMemberMapper.insert(ocCloudInstanceTaskMember);
    }

    @Override
    public void updateOcCloudInstanceTaskMember(OcCloudInstanceTaskMember ocCloudInstanceTaskMember) {
        ocCloudInstanceTaskMemberMapper.updateByPrimaryKey(ocCloudInstanceTaskMember);
    }

}
