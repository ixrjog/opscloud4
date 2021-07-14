package com.baiyi.opscloud.domain.vo.application;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:11 下午
 * @Version 1.0
 */
public class ApplicationVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Application extends BaseVO {

        private List<ApplicationResourceVO.Resource> resources;

        @ApiModelProperty(value = "主键",example="1")
        private Integer id;

        @ApiModelProperty(value = "应用名称")
        private String name;

        @ApiModelProperty(value = "应用关键字")
        private String applicationKey;

        private Integer applicationType;

        @ApiModelProperty(value = "描述")
        private String comment;

    }
}
