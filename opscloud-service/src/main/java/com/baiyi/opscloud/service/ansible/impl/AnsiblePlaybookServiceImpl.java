package com.baiyi.opscloud.service.ansible.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AnsiblePlaybook;
import com.baiyi.opscloud.mapper.opscloud.AnsiblePlaybookMapper;
import com.baiyi.opscloud.service.ansible.AnsiblePlaybookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/31 5:59 下午
 * @Version 1.0
 */
@Service
public class AnsiblePlaybookServiceImpl implements AnsiblePlaybookService {

    @Resource
    private AnsiblePlaybookMapper ansiblePlaybookMapper;

    @Override
    public void add(AnsiblePlaybook ansiblePlaybook) {
        ansiblePlaybookMapper.insert(ansiblePlaybook);
    }

    @Override
    public void update(AnsiblePlaybook ansiblePlaybook) {
        ansiblePlaybookMapper.updateByPrimaryKey(ansiblePlaybook);
    }

    @Override
    public void deleteById(int id) {
        ansiblePlaybookMapper.deleteByPrimaryKey(id);
    }
}
