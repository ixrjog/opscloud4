package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;
import com.baiyi.opscloud.mapper.opscloud.OcTerminalSessionInstanceMapper;
import com.baiyi.opscloud.service.terminal.OcTerminalSessionInstanceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/25 11:11 上午
 * @Version 1.0
 */
@Service
public class OcTerminalSessionInstanceServiceImpl implements OcTerminalSessionInstanceService {

    @Resource
    private OcTerminalSessionInstanceMapper ocTerminalSessionInstanceMapper;

    @Override
    public void addOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance) {
        ocTerminalSessionInstanceMapper.insert(ocTerminalSessionInstance);
    }

    @Override
    public void updateOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance) {
        ocTerminalSessionInstanceMapper.updateByPrimaryKey(ocTerminalSessionInstance);
    }

    @Override
    public OcTerminalSessionInstance queryOcTerminalSessionInstanceByUniqueKey(String sessionId, String instanceId) {
        Example example = new Example(OcTerminalSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        criteria.andEqualTo("instanceId", instanceId);
        return ocTerminalSessionInstanceMapper.selectOneByExample(example);
    }

    @Override
    public OcTerminalSessionInstance queryOcTerminalSessionInstanceById(int id) {
        return ocTerminalSessionInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OcTerminalSessionInstance> queryOcTerminalSessionInstanceBySessionId(String sessionId) {
        Example example = new Example(OcTerminalSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return ocTerminalSessionInstanceMapper.selectByExample(example);
    }

}
