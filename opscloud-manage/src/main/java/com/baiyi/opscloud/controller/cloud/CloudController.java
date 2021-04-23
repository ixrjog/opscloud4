package com.baiyi.opscloud.controller.cloud;

import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.vo.cloud.AliyunAccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/12 9:43 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud")
@Api(tags = "云通用管理")
public class CloudController {

    @Resource
    private AliyunCore aliyunCore;

    @ApiOperation(value = "查询阿里云主账户信息")
    @GetMapping(value = "/aliyun/account/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AliyunAccountVO.AliyunAccount>> queryAliyunAccount() {
        return new HttpResult<>(aliyunCore.queryAliyunAccount());
    }
}
