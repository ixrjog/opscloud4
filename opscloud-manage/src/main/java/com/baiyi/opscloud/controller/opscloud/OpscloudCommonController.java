package com.baiyi.opscloud.controller.opscloud;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudCommonParam;
import com.baiyi.opscloud.facade.opscloud.OpscloudCommonFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/16 3:11 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/opscloud/common")
@Api(tags = "OC通用服务")
public class OpscloudCommonController {

    @Resource
    private OpscloudCommonFacade opscloudCommonFacade;

    @ApiOperation(value = "中文转拼音")
    @PostMapping(value = "/topinyin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> chineseToPinYin(@RequestBody OpscloudCommonParam.ToPinYin param) {
        return new HttpResult<>(opscloudCommonFacade.chineseToPinYin(param));
    }
}
