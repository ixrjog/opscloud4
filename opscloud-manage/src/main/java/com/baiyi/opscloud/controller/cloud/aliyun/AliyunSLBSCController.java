package com.baiyi.opscloud.controller.cloud.aliyun;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.aliyun.AliyunSLBSCFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:48 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/aliyun/slb/sc")
@Api(tags = "阿里云SLB证书管理")
public class AliyunSLBSCController {

    @Resource
    private AliyunSLBSCFacade aliyunSLBSCFacade;

    @ApiOperation(value = "分页查询SLB证书")
    @PostMapping(value = "/page/query")
    public HttpResult<DataTable<AliyunSLBVO.ServerCertificate>> queryAliyunSlbSCPage(
            @RequestBody AliyunSLBParam.SCPageQuery pageQuery) {
        return new HttpResult<>(aliyunSLBSCFacade.queryAliyunSlbSCPage(pageQuery));
    }

    @ApiOperation(value = "同步SLB证书")
    @GetMapping(value = "/sync")
    public HttpResult<Boolean> syncSLBSC() {
        return new HttpResult<>(aliyunSLBSCFacade.syncSLBSC());
    }

    @ApiOperation(value = "设置更换的证书")
    @PostMapping(value = "/set")
    public HttpResult<Boolean> setUpdateSC(@RequestBody @Valid AliyunSLBParam.SetUpdateSC param) {
        return new HttpResult<>(aliyunSLBSCFacade.setUpdateSC(param));
    }

    @ApiOperation(value = "更换证书")
    @PostMapping(value = "/replace")
    public HttpResult<Boolean> replaceSC(@RequestBody @Valid AliyunSLBParam.ReplaceSC param) {
        return new HttpResult<>(aliyunSLBSCFacade.replaceSC(param));
    }
}
