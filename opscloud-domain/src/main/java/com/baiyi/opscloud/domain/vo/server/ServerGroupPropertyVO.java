package com.baiyi.opscloud.domain.vo.server;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/4 2:26 下午
 * @Version 1.0
 */
public class ServerGroupPropertyVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupProperty {

        private Integer id;
        private Integer serverGroupId;
        private Integer envType;
        private String propertyName;
        private String propertyValue;

    }

}
