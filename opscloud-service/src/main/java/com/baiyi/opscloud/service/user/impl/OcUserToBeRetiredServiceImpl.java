package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcUserToBeRetired;
import com.baiyi.opscloud.mapper.opscloud.OcUserToBeRetiredMapper;
import com.baiyi.opscloud.service.user.OcUserToBeRetiredService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/26 5:10 下午
 * @Since 1.0
 */

@Service
public class OcUserToBeRetiredServiceImpl implements OcUserToBeRetiredService {

    @Resource
    private OcUserToBeRetiredMapper ocUserToBeRetiredMapper;

    @Override
    public void addOcUserToBeRetired(OcUserToBeRetired ocUserToBeRetired) {
        ocUserToBeRetiredMapper.insert(ocUserToBeRetired);
    }

    @Override
    public List<OcUserToBeRetired> queryOcUserToBeRetiredAll() {
        return ocUserToBeRetiredMapper.selectAll();
    }

    @Override
    public void delOcUserToBeRetiredByUserId(Integer userId) {
        Example example = new Example(OcUserToBeRetired.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        ocUserToBeRetiredMapper.deleteByExample(example);
    }
}
