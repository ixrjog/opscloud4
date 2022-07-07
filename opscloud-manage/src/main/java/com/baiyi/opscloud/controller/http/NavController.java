package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.sys.NavVO;
import com.baiyi.opscloud.facade.sys.NavFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/6/30 10:22 PM
 * @Since 1.0
 */

@RestController
@RequestMapping("/api/sys/nav")
@Api(tags = "导航栏管理")
@RequiredArgsConstructor
public class NavController {

    private final NavFacade navFacade;

    @ApiOperation(value = "查询有效导航")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<NavVO.Nav>> listNav() {
        return new HttpResult<>(navFacade.ListActive());
    }

}
