package com.baiyi.opscloud.service.dingtalk.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalk;
import com.baiyi.opscloud.mapper.opscloud.OcDingtalkMapper;
import com.baiyi.opscloud.service.dingtalk.OcDingtalkService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818oup">修远</a>
 * @Date 2020/11/16 3:27 下午
 * @Since 1.0
 */

@Service
public class OcDingtalkServiceImpl implements OcDingtalkService {

    @Resource
    private OcDingtalkMapper ocDingtalkMapper;

    @Override
    public OcDingtalk queryOcDingtalkByKey(String dingtalkKey) {
        Example example = new Example(OcDingtalk.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dingtalkKey", dingtalkKey);
        return ocDingtalkMapper.selectOneByExample(example);
    }
}
