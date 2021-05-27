package com.baiyi.caesar.domain.param.server;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 1:18 下午
 * @Version 1.0
 */
public class ServerGroupTypeParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupTypePageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "名称")
        private String name;

        private Boolean extend;

    }
}
