package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTask;
import com.baiyi.opscloud.mapper.opscloud.OcCloudInstanceTaskMapper;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:42 上午
 * @Version 1.0
 */
@Service
public class OcCloudInstanceTaskServiceImpl implements OcCloudInstanceTaskService {

    @Resource
    private OcCloudInstanceTaskMapper ocCloudInstanceTaskMapper;

    @Override
    public void addOcCloudInstanceTask(OcCloudInstanceTask ocCloudInstanceTask) {
        ocCloudInstanceTaskMapper.insert(ocCloudInstanceTask);
    }

    @Override
    public void updateOcCloudInstanceTask(OcCloudInstanceTask ocCloudInstanceTask) {
        ocCloudInstanceTaskMapper.updateByPrimaryKey(ocCloudInstanceTask);
    }

    @Override
    public OcCloudInstanceTask queryOcCloudInstanceTaskById(int id) {
        return ocCloudInstanceTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcCloudInstanceTask queryLastOcCloudInstanceTaskByTemplateId(int templateId){
       return ocCloudInstanceTaskMapper.queryLastOcCloudInstanceTaskByTemplateId(templateId);
    }
}
