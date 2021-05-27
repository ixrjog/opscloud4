package com.baiyi.caesar.domain.param.caesar;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/9/8 1:55 下午
 * @Version 1.0
 */
public class CaesarInstanceParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class CaesarInstancePageQuery extends PageParam {

    }

}
