package com.baiyi.opscloud.controller.opscloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudInstanceParam;
import com.baiyi.opscloud.domain.vo.opscloud.HealthVO;
import com.baiyi.opscloud.domain.vo.opscloud.OpscloudVO;
import com.baiyi.opscloud.facade.opscloud.OpscloudInstanceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/24 4:31 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/opscloud")
@Api(tags = "OC管理")
public class OpscloudController {

    @Resource
    private OpscloudInstanceFacade opscloudInstanceFacade;

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class ResourceInactiveException extends RuntimeException {
        // OpscloudInstance维护模式
    }

    @ApiOperation(value = "负载均衡健康检查接口")
    @GetMapping(value = "/health/slb-health-check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<HealthVO.Health> checkHealth() {
        HealthVO.Health health = opscloudInstanceFacade.checkHealth();
        if (health.isHealth()) {
            return new HttpResult<>(opscloudInstanceFacade.checkHealth());
        } else {
            throw new ResourceInactiveException();
        }
    }

    @ApiOperation(value = "分页查询OC实例配置")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OpscloudVO.Instance>> queryOpscloudInstancePage(@RequestBody @Valid OpscloudInstanceParam.PageQuery pageQuery) {
        return new HttpResult<>(opscloudInstanceFacade.queryOpscloudInstancePage(pageQuery));
    }

    @ApiOperation(value = "设置OC实例是否有效")
    @GetMapping(value = "/instance/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setOpscloudInstanceActive(@RequestParam int id) {
        return new HttpResult<>(opscloudInstanceFacade.setOpscloudInstanceActive(id));
    }

    @ApiOperation(value = "删除OC实例")
    @DeleteMapping(value = "/instance/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delOpscloudInstanceById(@RequestParam int id) {
        return new HttpResult<>(opscloudInstanceFacade.delOpscloudInstanceById(id));
    }

}
