package com.baiyi.opscloud.service.aliyun.slb.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunSlbScMapper;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbSCService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:47 下午
 * @Since 1.0
 */

@Service
public class OcAliyunSlbSCServiceImpl implements OcAliyunSlbSCService {

    @Resource
    private OcAliyunSlbScMapper ocAliyunSlbScMapper;

    @Override
    public OcAliyunSlbSc queryOcAliyunSlbScById(Integer id) {
        return ocAliyunSlbScMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAliyunSlbSc(OcAliyunSlbSc ocAliyunSlbSc) {
        ocAliyunSlbScMapper.insert(ocAliyunSlbSc);

    }

    @Override
    public void updateOcAliyunSlbSc(OcAliyunSlbSc ocAliyunSlbSc) {
        ocAliyunSlbScMapper.updateByPrimaryKey(ocAliyunSlbSc);
    }

    @Override
    public void deleteOcAliyunSlbSc(int id) {
        ocAliyunSlbScMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcAliyunSlbSc queryOcAliyunSlbScBySCId(String scId) {
        Example example = new Example(OcAliyunSlbSc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverCertificateId", scId);
        return ocAliyunSlbScMapper.selectOneByExample(example);    }

    @Override
    public List<OcAliyunSlbSc> queryOcAliyunSlbScAll() {
        return ocAliyunSlbScMapper.selectAll();
    }

    @Override
    public DataTable<OcAliyunSlbSc> queryOcAliyunSlbScPage(AliyunSLBParam.SCPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunSlbSc> slbServerCertificateList = ocAliyunSlbScMapper.queryOcAliyunSlbScPage(pageQuery);
        return new DataTable<>(slbServerCertificateList, page.getTotal());
    }
}
