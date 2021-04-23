package com.baiyi.opscloud.cloud.slb;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 4:08 下午
 * @Since 1.0
 */
public interface AliyunSLBSCCenter {

    BusinessWrapper<Boolean> syncSLBSC();

    BusinessWrapper<Boolean> setUpdateSC(AliyunSLBParam.SetUpdateSC param);

    BusinessWrapper<Boolean> replaceSC(AliyunSLBParam.ReplaceSC param);
}
