package com.baiyi.opscloud.domain.param.opscloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/16 3:00 下午
 * @Since 1.0
 */
public class OpscloudCommonParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ToPinYin {

        private String text;

    }
}
