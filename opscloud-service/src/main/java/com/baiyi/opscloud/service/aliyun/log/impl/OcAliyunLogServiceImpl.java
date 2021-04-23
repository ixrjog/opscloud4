package com.baiyi.opscloud.service.aliyun.log.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunLog;
import com.baiyi.opscloud.domain.param.cloud.AliyunLogParam;
import com.baiyi.opscloud.mapper.opscloud.OcAliyunLogMapper;
import com.baiyi.opscloud.service.aliyun.log.OcAliyunLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/13 3:36 下午
 * @Version 1.0
 */
@Service
public class OcAliyunLogServiceImpl implements OcAliyunLogService {

    @Resource
    private OcAliyunLogMapper ocAliyunLogMapper;

    @Override
    public DataTable<OcAliyunLog> queryOcAliyunLogByParam(AliyunLogParam.AliyunLogPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcAliyunLog> list = ocAliyunLogMapper.queryOcAliyunLogByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcAliyunLog(OcAliyunLog ocAliyunLog) {
        ocAliyunLogMapper.insert(ocAliyunLog);
    }

    @Override
    public void updateOcAliyunLog(OcAliyunLog ocAliyunLog) {
        ocAliyunLogMapper.updateByPrimaryKey(ocAliyunLog);
    }


    @Override
    public OcAliyunLog queryOcAliyunLogById(int id) {
        return ocAliyunLogMapper.selectByPrimaryKey(id);
    }

}
