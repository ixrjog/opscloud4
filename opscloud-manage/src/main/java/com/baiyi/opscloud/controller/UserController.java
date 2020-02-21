package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.UserFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:09 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Resource
    private UserFacade userFacade;

    /**
     * 管理员查询
     *
     * @param pageQuery
     * @return
     */
    @ApiOperation(value = "分页查询user列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcUserVO.OcUser>> queryUserPage(@Valid UserParam.PageQuery pageQuery) {
        return new HttpResult<>(userFacade.queryUserPage(pageQuery));
    }
}