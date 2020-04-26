package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderGroupVO;
import com.baiyi.opscloud.facade.WorkorderFacade;
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
 * @Date 2020/4/26 1:09 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/workorder")
@Api(tags = "工单")
public class WorkorderController {

    @Resource
    private WorkorderFacade workorderFacade;

    @ApiOperation(value = "分页工单组列表")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcWorkorderGroupVO.WorkorderGroup>> queryWorkorderGroupPage(@RequestBody @Valid WorkorderGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(workorderFacade.queryWorkorderGroupPage(pageQuery));
    }

}
