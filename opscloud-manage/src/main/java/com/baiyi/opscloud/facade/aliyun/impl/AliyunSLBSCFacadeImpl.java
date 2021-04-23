package com.baiyi.opscloud.facade.aliyun.impl;

import com.baiyi.opscloud.cloud.slb.AliyunSLBSCCenter;
import com.baiyi.opscloud.decorator.aliyun.slb.AliyunSLBSCDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.aliyun.AliyunSLBSCFacade;
import com.baiyi.opscloud.service.aliyun.slb.OcAliyunSlbSCService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:38 下午
 * @Since 1.0
 */

@Component("AliyunSLBSCFacade")
public class AliyunSLBSCFacadeImpl implements AliyunSLBSCFacade {

    @Resource
    private AliyunSLBSCCenter aliyunSLBSCCenter;

    @Resource
    private OcAliyunSlbSCService ocAliyunSlbSCService;

    @Resource
    private AliyunSLBSCDecorator aliyunSLBSCDecorator;

    @Override
    public BusinessWrapper<Boolean> syncSLBSC() {
        return aliyunSLBSCCenter.syncSLBSC();
    }

    @Override
    public DataTable<AliyunSLBVO.ServerCertificate> queryAliyunSlbSCPage(AliyunSLBParam.SCPageQuery pageQuery) {
        DataTable<OcAliyunSlbSc> table = ocAliyunSlbSCService.queryOcAliyunSlbScPage(pageQuery);
        List<AliyunSLBVO.ServerCertificate> serverCertificateList = aliyunSLBSCDecorator.decoratorVOList(table.getData());
        return new DataTable<>(serverCertificateList, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> setUpdateSC(AliyunSLBParam.SetUpdateSC param) {
        return aliyunSLBSCCenter.setUpdateSC(param);
    }

    @Override
    public BusinessWrapper<Boolean> replaceSC(AliyunSLBParam.ReplaceSC param) {
        return aliyunSLBSCCenter.replaceSC(param);
    }
}
