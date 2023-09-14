package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.example.ExampleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2023/9/14 13:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/example")
@Tag(name = "例子")
@RequiredArgsConstructor
public class ExampleController {

    @Operation(summary = "Hello World")
    @PostMapping(value = "/helloWorld", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ExampleVO.HelloWorld> helloWorld() {
        return new HttpResult<>(ExampleVO.HelloWorld.builder().build());
    }

}
