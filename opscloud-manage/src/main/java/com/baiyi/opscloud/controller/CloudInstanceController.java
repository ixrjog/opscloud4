package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.facade.CloudInstanceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:37 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/instance")
@Api(tags = "云实例管理")
public class CloudInstanceController {

    @Resource
    private CloudInstanceFacade cloudInstanceFacade;

    @ApiOperation(value = "分页模糊查询云实例模版列表")
    @PostMapping(value = "/template/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate>> queryCloudInstanceTemplatePage(@RequestBody @Valid CloudInstanceTemplateParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudInstanceFacade.fuzzyQueryCloudInstanceTemplatePage(pageQuery));
    }

}
