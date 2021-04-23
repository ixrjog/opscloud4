package com.baiyi.opscloud.facade.opscloud.impl;

import com.baiyi.opscloud.common.util.PinYinUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudCommonParam;
import com.baiyi.opscloud.facade.opscloud.OpscloudCommonFacade;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/16 2:59 下午
 * @Since 1.0
 */

@Component
public class OpscloudCommonFacadeImpl implements OpscloudCommonFacade {

    @Override
    public BusinessWrapper<String> chineseToPinYin(OpscloudCommonParam.ToPinYin param) {
        return new BusinessWrapper<>(PinYinUtils.toPinYin(param.getText()));
    }
}
