package com.baiyi.opscloud.service.org.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcOrgDepartmentMember;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.mapper.opscloud.OcOrgDepartmentMemberMapper;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:25 下午
 * @Version 1.0
 */
@Service
public class OcOrgDepartmentMemberServiceImpl implements OcOrgDepartmentMemberService {

    @Resource
    private OcOrgDepartmentMemberMapper ocOrgDepartmentMemberMapper;

    @Override
    public DataTable<OcOrgDepartmentMember> queryOcOrgDepartmentMemberParam(DepartmentMemberParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcOrgDepartmentMember> list = ocOrgDepartmentMemberMapper.queryOcOrgDepartmentMemberParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }
}
