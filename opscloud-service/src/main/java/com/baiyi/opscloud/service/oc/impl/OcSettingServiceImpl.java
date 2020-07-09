package com.baiyi.opscloud.service.oc.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcSetting;
import com.baiyi.opscloud.mapper.opscloud.OcSettingMapper;
import com.baiyi.opscloud.service.oc.OcSettingService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/4 4:18 下午
 * @Version 1.0
 */
@Service
public class OcSettingServiceImpl implements OcSettingService {

    @Resource
    private OcSettingMapper ocSettingMapper;

    @Override
    public OcSetting queryOcSettingByName(String name) {
        Example example = new Example(OcSetting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return ocSettingMapper.selectOneByExample(example);
    }

    @Override
    public List<OcSetting> queryAll() {
        return ocSettingMapper.selectAll();
    }

    @Override
    public void updateOcSetting(OcSetting ocSetting) {
        ocSettingMapper.updateByPrimaryKey(ocSetting);
    }
}
