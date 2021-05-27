package com.baiyi.caesar.service.server.impl;

import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.param.server.ServerAccountParam;
import com.baiyi.caesar.mapper.caesar.ServerAccountMapper;
import com.baiyi.caesar.service.server.ServerAccountService;
import com.baiyi.caesar.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:16 上午
 * @Version 1.0
 */
@Service
public class ServerAccountServiceImpl implements ServerAccountService {

    @Resource
    private ServerAccountMapper accountMapper;

    @Override
    public ServerAccount getById(Integer id){
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(ServerAccount serverAccount) {
        accountMapper.insert(serverAccount);
    }

    @Override
    public void update(ServerAccount serverAccount) {
        accountMapper.updateByPrimaryKey(serverAccount);
    }

    @Override
    public DataTable<ServerAccount> queryPageByParam(ServerAccountParam.ServerAccountPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(ServerAccount.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getUsername()))
            criteria.andLike("username", SQLUtil.toLike(pageQuery.getUsername()));
        if (IdUtil.isNotEmpty(pageQuery.getAccountType()))
            criteria.andEqualTo("accountType", pageQuery.getAccountType());
        if (StringUtils.isNotBlank(pageQuery.getProtocol())) {
            criteria.andEqualTo("protocol", pageQuery.getProtocol());
        }
        List<ServerAccount> data = accountMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }


}
