package com.baiyi.opscloud.domain.param.monitor;

import com.baiyi.opscloud.domain.param.account.AccountParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/12/9 10:58 上午
 * @Version 1.0
 */
public class MonitorUserParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MonitorUserPageQuery extends AccountParam.AccountPageQuery {

    }
}
