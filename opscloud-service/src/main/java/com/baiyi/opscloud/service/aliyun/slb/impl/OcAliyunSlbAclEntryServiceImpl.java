package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclEntry;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbAclEntryMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbAclEntryService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:07 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSlbAclEntryServiceImpl implements OcAliyunSlbAclEntryService {

    @Resource
    private OcAliyunSlbAclEntryMapper ocAliyunSlbAclEntryMapper;

    @Override
    public void addOcAliyunSlbAclEntryList(List<OcAliyunSlbAclEntry> ocAliyunSlbAclEntryList) {
        ocAliyunSlbAclEntryMapper.insertList(ocAliyunSlbAclEntryList);
    }

    @Override
    public void deleteOcAliyunSlbAclEntryBySlbAclId(String slbAclId) {
        Example example = new Example(OcAliyunSlbAclEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("slbAclId", slbAclId);
        ocAliyunSlbAclEntryMapper.deleteByExample(example);
    }

    @Override
    public List<OcAliyunSlbAclEntry> queryOcAliyunSlbAclEntryBySlbAclId(String slbAclId) {
        Example example = new Example(OcAliyunSlbAclEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("slbAclId", slbAclId);
        return ocAliyunSlbAclEntryMapper.selectByExample(example);
    }
}
