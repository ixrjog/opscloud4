package com.baiyi.opscloud.service.ram.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcOrgDepartment;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunRamUserMapper;
import com.baiyi.opscloud.service.ram.OcAliyunRamUserService;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 1:48 下午
 * @Version 1.0
 */
@Component
public class OcAliyunRamUserServiceImpl implements OcAliyunRamUserService {

    @Resource
    private OcAliyunRamUserMapper ocAliyunRamUserMapper;

    @Override
    public List<OcAliyunRamUser> queryOcAliyunRamUserByAccountUid(String accountUid) {
        Example example = new Example(OcOrgDepartment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid);
        return ocAliyunRamUserMapper.selectByExample(example);
    }

    @Override
    public OcAliyunRamUser queryOcAliyunRamUserByUniqueKey(String accountUid, String ramUsername) {
        Example example = new Example(OcOrgDepartment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid);
        criteria.andEqualTo("ramUsername", ramUsername);
        return ocAliyunRamUserMapper.selectOneByExample(example);
    }

    @Override
    public void addOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser) {
        ocAliyunRamUserMapper.insert(ocAliyunRamUser);
    }

    @Override
    public void updateOcAliyunRamUser(OcAliyunRamUser ocAliyunRamUser) {
        ocAliyunRamUserMapper.updateByPrimaryKey(ocAliyunRamUser);
    }

    @Override
    public void deleteOcAliyunRamUserById(int id) {
        ocAliyunRamUserMapper.deleteByPrimaryKey(id);
    }


}
