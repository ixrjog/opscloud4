package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.facade.business.BusinessPropertyFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/8/20 2:07 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/business")
@Api(tags = "业务对象管理")
public class BusinessController {

    @Resource
    private BusinessPropertyFacade businessPropertyFacade;

    @ApiOperation(value = "新增业务对象属性配置")
    @PostMapping(value = "/property/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addBusinessProperty(@RequestBody @Valid BusinessPropertyVO.Property property) {
        businessPropertyFacade.add(property);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新业务对象属性配置")
    @PutMapping(value = "/property/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessProperty(@RequestBody @Valid BusinessPropertyVO.Property property) {
        businessPropertyFacade.update(property);
        return HttpResult.SUCCESS;
    }

}
