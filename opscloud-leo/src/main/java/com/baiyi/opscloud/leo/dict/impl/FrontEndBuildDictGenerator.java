package com.baiyi.opscloud.leo.dict.impl;

import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/6/21 14:24
 * @Version 1.0
 */
@Slf4j
@Component
public class FrontEndBuildDictGenerator extends BaseBuildDictGenerator {

    @Override
    public String getBuildType() {
        return BuildTypeConstants.FRONT_END;
    }

}