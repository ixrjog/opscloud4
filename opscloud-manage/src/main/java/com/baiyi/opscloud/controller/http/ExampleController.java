package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.example.ExampleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2023/9/14 13:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/example")
@Tag(name = "接口例子")
@RequiredArgsConstructor
public class ExampleController {

    @Operation(summary = "Example for Hello World")
    @GetMapping(value = "/helloWorld", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ExampleVO.HelloWorld> helloWorld() {
        return new HttpResult<>(ExampleVO.HelloWorld.EXAMPLE);
    }

}