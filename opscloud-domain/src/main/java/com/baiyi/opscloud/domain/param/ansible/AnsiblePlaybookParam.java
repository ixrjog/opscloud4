package com.baiyi.opscloud.domain.param.ansible;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/9/1 10:55 上午
 * @Version 1.0
 */
public class AnsiblePlaybookParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class AnsiblePlaybookPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "剧本名称")
        private String name;

        private Boolean extend;

    }
}
