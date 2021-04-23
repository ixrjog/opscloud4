package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSLBService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:39 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSLBServiceImpl implements OcAliyunSLBService {

    @Resource
    private OcAliyunSlbMapper ocAliyunSlbMapper;

    @Override
    public OcAliyunSlb queryOcAliyunSlbById(Integer id) {
        return ocAliyunSlbMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunSlb(OcAliyunSlb ocAliyunSlb) {
        ocAliyunSlbMapper.insert(ocAliyunSlb);
    }

    @Override
    public void updateOcAliyunSlb(OcAliyunSlb ocAliyunSlb) {
        ocAliyunSlbMapper.updateByPrimaryKey(ocAliyunSlb);
    }

    @Override
    public void deleteOcAliyunSlb(int id) {
        ocAliyunSlbMapper.deleteByPrimaryKey(id);
    }


    @Override
    public OcAliyunSlb queryOcAliyunSlbByLoadBalancerId(String loadBalancerId) {
        Example example = new Example(OcAliyunSlb.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loadBalancerId", loadBalancerId);
        return ocAliyunSlbMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAliyunSlb> queryOcAliyunSlbByLoadBalancerIdList(List<String> loadBalancerIdList) {
        return ocAliyunSlbMapper.queryOcAliyunSlbByLoadBalancerIdList(loadBalancerIdList);
    }

    @Override
    public List<OcAliyunSlb> queryOcAliyunSlbAll() {
        return ocAliyunSlbMapper.selectAll();
    }

    @Override
    public DataTable<OcAliyunSlb> queryOcAliyunSlbPage(AliyunSLBParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunSlb> slbList = ocAliyunSlbMapper.queryOcAliyunSlbPage(pageQuery);
        return new DataTable<>(slbList, page.getTotal());
    }

    @Override
    public List<OcAliyunSlb> queryLinkNginxSLB() {
        Example example = new Example(OcAliyunSlb.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isLinkNginx", 1);
        return ocAliyunSlbMapper.selectByExample(example);
    }

}
