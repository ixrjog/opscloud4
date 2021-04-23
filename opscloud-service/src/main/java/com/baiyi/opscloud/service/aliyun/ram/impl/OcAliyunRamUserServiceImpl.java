package com.baiyi.opscloud.service.aliyun.ram.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.param.cloud.AliyunRAMUserParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunRamUserMapper;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 1:48 下午
 * @Version 1.0
 */
@Service
public class OcAliyunRamUserServiceImpl implements OcAliyunRamUserService {

    @Resource
    private OcAliyunRamUserMapper ocAliyunRamUserMapper;

    @Override
    public List<OcAliyunRamUser> queryOcAliyunRamUserByAccountUid(String accountUid) {
        Example example = new Example(OcAliyunRamUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid);
        return ocAliyunRamUserMapper.selectByExample(example);
    }

    @Override
    public OcAliyunRamUser queryOcAliyunRamUserByUniqueKey(String accountUid, String ramUsername) {
        Example example = new Example(OcAliyunRamUser.class);
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

    @Override
    public DataTable<OcAliyunRamUser> queryOcAliyunRamUserByParam(AliyunRAMUserParam.RamUserPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunRamUser> list = ocAliyunRamUserMapper.queryOcAliyunRamUserByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcAliyunRamUser> queryUserPermissionRamUserByUserId(int userId) {
        return ocAliyunRamUserMapper.queryUserPermissionRamUserByUserId(userId);
    }


}
