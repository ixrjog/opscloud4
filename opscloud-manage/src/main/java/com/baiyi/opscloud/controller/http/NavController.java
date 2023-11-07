package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.sys.NavVO;
import com.baiyi.opscloud.facade.sys.NavFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "导航栏管理")
@RequiredArgsConstructor
public class NavController {

    private final NavFacade navFacade;

    @Operation(summary = "查询有效导航")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<NavVO.Nav>> listNav() {
        return new HttpResult<>(navFacade.listActive());
    }

}