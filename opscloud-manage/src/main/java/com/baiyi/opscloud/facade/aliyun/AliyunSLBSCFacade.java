package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:38 下午
 * @Since 1.0
 */
public interface AliyunSLBSCFacade {

    BusinessWrapper<Boolean> syncSLBSC();

    DataTable<AliyunSLBVO.ServerCertificate> queryAliyunSlbSCPage(AliyunSLBParam.SCPageQuery pageQuery);

    BusinessWrapper<Boolean> setUpdateSC(AliyunSLBParam.SetUpdateSC param);

    BusinessWrapper<Boolean> replaceSC(AliyunSLBParam.ReplaceSC param);
}
