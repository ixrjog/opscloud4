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
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "组名")
        private String name;

        @ApiModelProperty(value = "组类型")
        private Integer serverGroupTypeId;

        private Boolean extend;
    }

}
