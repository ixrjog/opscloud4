package com.baiyi.opscloud.facade.opscloud;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudCommonParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/16 2:58 下午
 * @Since 1.0
 */
public interface OpscloudCommonFacade {

    BusinessWrapper<String> chineseToPinYin(OpscloudCommonParam.ToPinYin param);
}
