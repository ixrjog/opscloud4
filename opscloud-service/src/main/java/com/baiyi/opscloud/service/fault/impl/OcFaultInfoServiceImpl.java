package com.baiyi.opscloud.service.fault.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcFaultInfo;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import com.baiyi.opscloud.mapper.opscloud.OcFaultInfoMapper;
import com.baiyi.opscloud.service.fault.OcFaultInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 3:52 下午
 * @Since 1.0
 */

@Service
public class OcFaultInfoServiceImpl implements OcFaultInfoService {

    @Resource
    private OcFaultInfoMapper ocFaultInfoMapper;

    @Override
    public OcFaultInfo queryOcFaultInfo(int id) {
        return ocFaultInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcFaultInfo(OcFaultInfo ocFaultInfo) {
        ocFaultInfoMapper.insert(ocFaultInfo);
    }

    @Override
    public void updateOcFaultInfo(OcFaultInfo ocFaultInfo) {
        ocFaultInfoMapper.updateByPrimaryKey(ocFaultInfo);
    }

    @Override
    public void delOcFaultInfo(int id) {
        ocFaultInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<OcFaultInfo> queryOcFaultInfoPage(FaultParam.InfoPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcFaultInfo> ocFaultInfoList = ocFaultInfoMapper.queryOcFaultInfoPage(pageQuery);
        return new DataTable<>(ocFaultInfoList, page.getTotal());
    }
}
