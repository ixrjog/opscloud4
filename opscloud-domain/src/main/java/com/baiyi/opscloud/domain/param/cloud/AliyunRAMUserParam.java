package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/9 5:37 下午
 * @Version 1.0
 */
public class AliyunRAMUserParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class RamUserPageQuery extends PageParam {

        @ApiModelProperty(value = "主账户uid")
        private String accountUid;

        @ApiModelProperty(value = "是否有AK")
        private Boolean hasKeys;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        @ApiModelProperty(value = "扩展属性")
        private Integer extend;

    }
}
