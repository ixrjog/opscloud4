package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:10 下午
 * @Since 1.0
 */
public class AliyunDomainParam {


    @Data
    @ApiModel
    @NoArgsConstructor
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "域名")
        private String domainName;

        @ApiModelProperty(value = "是否活跃")
        @NotNull(message = "是否活跃不能为空")
        private Integer isActive;

    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class SetActive {

        @ApiModelProperty(value = "id主键")
        @NotNull(message = "id主键不能为空")
        private Integer id;

    }
}
