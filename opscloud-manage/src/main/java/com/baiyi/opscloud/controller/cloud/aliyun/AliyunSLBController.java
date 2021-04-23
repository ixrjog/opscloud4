package com.baiyi.opscloud.controller.cloud.aliyun;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.aliyun.AliyunSLBFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:47 下午
 * @Since 1.0
 */
@RestController
@RequestMapping("/aliyun/slb")
@Api(tags = "阿里云SLB管理")
public class AliyunSLBController {

    @Resource
    private AliyunSLBFacade aliyunSLBFacade;

    @ApiOperation(value = "分页查询SLB")
    @PostMapping(value = "/page/query")
    public HttpResult<DataTable<AliyunSLBVO.LoadBalancer>> queryAliyunSLBPage(@RequestBody AliyunSLBParam.PageQuery pageQuery) {
        return new HttpResult<>(aliyunSLBFacade.queryAliyunSLBPage(pageQuery));
    }

    @ApiOperation(value = "同步SLB")
    @GetMapping(value = "/sync")
    public HttpResult<Boolean> syncAliyunSLB() {
        return new HttpResult<>(aliyunSLBFacade.syncSLB());
    }

    @ApiOperation(value = "刷新slb监听")
    @GetMapping(value = "/refresh/listener")
    public HttpResult<Boolean> refreshSLBListener(@RequestParam String loadBalancerId) {
        return new HttpResult<>(aliyunSLBFacade.refreshSLBListener(loadBalancerId));
    }

    @ApiOperation(value = "关联nginx配置")
    @GetMapping(value = "/link/nginx")
    public HttpResult<Boolean> linkNginx(@RequestParam Integer id) {
        return new HttpResult<>(aliyunSLBFacade.linkNginx(id));
    }

    @ApiOperation(value = "查询关联nginx配置")
    @GetMapping(value = "/link/nginx/query")
    public HttpResult<List<OcAliyunSlb>> queryLinkNginxSLB() {
        return new HttpResult<>(aliyunSLBFacade.queryLinkNginxSLB());
    }

    @ApiOperation(value = "查询slb后端服务器")
    @GetMapping(value = "/backend/server/query")
    public HttpResult<Map<Integer, List<AliyunSLBVO.BackendServer>>> querySLBListenerBackendServers(@RequestParam String loadBalancerId) {
        return new HttpResult<>(aliyunSLBFacade.querySLBListenerBackendServers(loadBalancerId));
    }

}
