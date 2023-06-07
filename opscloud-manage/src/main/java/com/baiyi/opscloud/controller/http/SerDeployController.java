package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.facade.ser.SerDeployFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 修远
 * @Date 2023/6/7 11:20 AM
 * @Since 1.0
 */

@RestController
@RequestMapping("/api/ser/")
@Tag(name = "ser包发布管理")
@RequiredArgsConstructor
public class SerDeployController {

    private final SerDeployFacade serDeployFacade;

    @Operation(summary = "ser包上传")
    @PostMapping(value = "/upload")
    public HttpResult<Boolean> uploadFile(@RequestBody MultipartFile file) {
        serDeployFacade.uploadFile(file);
        return HttpResult.SUCCESS;
    }


}
