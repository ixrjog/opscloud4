package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.RegisteredInstanceParam;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.facade.sys.InstanceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/9/3 2:12 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/instance")
@Tag(name = "实例管理")
@RequiredArgsConstructor
public class InstanceController {

    private final InstanceFacade instanceFacade;

    @Operation(summary = "分页查询注册实例列表")
    @PostMapping(value = "/registered/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<InstanceVO.RegisteredInstance>> queryRegisteredInstancePage(@RequestBody @Valid RegisteredInstanceParam.RegisteredInstancePageQuery pageQuery) {
        return new HttpResult<>(instanceFacade.queryRegisteredInstancePage(pageQuery));
    }

    @Operation(summary = "设置注册实例的有效/无效")
    @PutMapping(value = "/registered/active/set", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setRegisteredInstanceActive(@RequestParam @Valid int id) {
        instanceFacade.setRegisteredInstanceActive(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "负载均衡健康检查接口")
    @GetMapping(value = "/health/lb-check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<InstanceVO.Health> checkHealth() {
        InstanceVO.Health health = instanceFacade.checkHealth();
        if (health.isHealth()) {
            return new HttpResult<>(health);
        }
        throw new ResourceInactiveException();
    }

    /**
     * 服务不可用 HTTP 503 Service Unavailable
     */
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public static class ResourceInactiveException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 5764195967750671243L;
    }

}