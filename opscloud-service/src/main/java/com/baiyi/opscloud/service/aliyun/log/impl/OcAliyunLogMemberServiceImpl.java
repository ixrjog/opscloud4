package com.baiyi.opscloud.service.aliyun.log.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLogMember;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogMemberParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunLogMemberMapper;
import com.baiyi.opscloud.service.aliyun.log.OcAliyunLogMemberService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/15 9:24 上午
 * @Version 1.0
 */
@Service
public class OcAliyunLogMemberServiceImpl implements OcAliyunLogMemberService {

    @Resource
    private OcAliyunLogMemberMapper ocAliyunLogMemberMapper;

    @Override
    public DataTable<OcAliyunLogMember> queryOcAliyunLogMemberByParam(AliyunLogMemberParam.LogMemberPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunLogMember> list = ocAliyunLogMemberMapper.queryOcAliyunLogMemberByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcAliyunLogMember> queryOcAliyunLogMemberByLogId(int logId) {
        Example example = new Example(OcAliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("logId", logId);
        return ocAliyunLogMemberMapper.selectByExample(example);
    }

    @Override
    public  List<OcAliyunLogMember> queryOcAliyunLogMemberByServerGroupId(int serverGroupId){
        Example example = new Example(OcAliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return ocAliyunLogMemberMapper.selectByExample(example);
    }

    @Override
    public int countOcAliyunLogMemberByLogId(int logId) {
        Example example = new Example(OcAliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("logId", logId);
        return ocAliyunLogMemberMapper.selectCountByExample(example);
    }

    @Override
    public void addOcAliyunLogMember(OcAliyunLogMember ocAliyunLogMember) {
        ocAliyunLogMemberMapper.insert(ocAliyunLogMember);
    }

    @Override
    public void updateOcAliyunLogMember(OcAliyunLogMember ocAliyunLogMember) {
        ocAliyunLogMemberMapper.updateByPrimaryKey(ocAliyunLogMember);
    }

    @Override
    public void deleteOcAliyunLogMemberById(int id) {
        ocAliyunLogMemberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAliyunLogMember queryOcAliyunLogMemberByUniqueKey(OcAliyunLogMember ocAliyunLogMember) {
        Example example = new Example(OcAliyunLogMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("logId", ocAliyunLogMember.getLogId());
        criteria.andEqualTo("serverGroupId", ocAliyunLogMember.getServerGroupId());
        return ocAliyunLogMemberMapper.selectOneByExample(example);
    }

    @Override
    public OcAliyunLogMember queryOcAliyunLogMemberById(int id) {
        return ocAliyunLogMemberMapper.selectByPrimaryKey(id);
    }

}
