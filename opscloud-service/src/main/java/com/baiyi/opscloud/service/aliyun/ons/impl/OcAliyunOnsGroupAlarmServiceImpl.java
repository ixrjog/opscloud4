package com.baiyi.opscloud.service.aliyun.ons.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroupAlarm;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunOnsGroupAlarmMapper;
import com.baiyi.opscloud.service.aliyun.ons.OcAliyunOnsGroupAlarmService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/13 4:20 下午
 * @Since 1.0
 */

@Service
public class OcAliyunOnsGroupAlarmServiceImpl implements OcAliyunOnsGroupAlarmService {

    @Resource
    private OcAliyunOnsGroupAlarmMapper ocAliyunOnsGroupAlarmMapper;

    @Override
    public void addOcAliyunOnsGroupAlarm(OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm) {
        ocAliyunOnsGroupAlarmMapper.insert(ocAliyunOnsGroupAlarm);
    }

    @Override
    public void updateOcAliyunOnsGroupAlarm(OcAliyunOnsGroupAlarm ocAliyunOnsGroupAlarm) {
        ocAliyunOnsGroupAlarmMapper.updateByPrimaryKey(ocAliyunOnsGroupAlarm);
    }

    @Override
    public void deleteOcAliyunOnsGroupAlarmById(int id) {
        ocAliyunOnsGroupAlarmMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcAliyunOnsGroupAlarm> queryOcAliyunOnsGroupAlarmByStatus(Integer alarmStatus) {
        Example example = new Example(OcAliyunOnsGroupAlarm.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("alarmStatus", alarmStatus);
        return ocAliyunOnsGroupAlarmMapper.selectByExample(example);
    }

    @Override
    public OcAliyunOnsGroupAlarm queryOcAliyunOnsGroupByOnsGroupId(Integer onsGroupId) {
        Example example = new Example(OcAliyunOnsGroupAlarm.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("onsGroupId", onsGroupId);
        return ocAliyunOnsGroupAlarmMapper.selectOneByExample(example);
    }
}
