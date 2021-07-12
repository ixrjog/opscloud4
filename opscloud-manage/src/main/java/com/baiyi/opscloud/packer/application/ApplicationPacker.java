package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:23 下午
 * @Version 1.0
 */
@Component
public class ApplicationPacker {

    public List<ApplicationVO.Application> wrapVOList(List<Application> data) {
        return BeanCopierUtil.copyListProperties(data,ApplicationVO.Application.class);
    }

    public List<ApplicationVO.Application> wrapVOList(List<Application> data, IExtend iExtend) {
        return BeanCopierUtil.copyListProperties(data,ApplicationVO.Application.class);
    }

}
