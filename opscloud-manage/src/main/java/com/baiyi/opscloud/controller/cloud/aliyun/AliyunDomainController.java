package com.baiyi.opscloud.controller.cloud.aliyun;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDomainVO;
import com.baiyi.opscloud.facade.aliyun.AliyunDomainFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 4:31 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/aliyun/domain")
@Api(tags = "阿里云域名管理")
public class AliyunDomainController {

    @Resource
    private AliyunDomainFacade aliyunDomainFacade;

    @ApiOperation(value = "分页查询域名")
    @PostMapping(value = "/page/query")
    public HttpResult<DataTable<AliyunDomainVO.Domain>> queryAliyunDomainPage(@RequestBody AliyunDomainParam.PageQuery pageQuery) {
        return new HttpResult<>(aliyunDomainFacade.queryAliyunDomainPage(pageQuery));
    }

    @ApiOperation(value = "同步域名信息")
    @GetMapping(value = "/sync")
    public HttpResult<Boolean> syncAliyunDomain() {
        return new HttpResult<>(aliyunDomainFacade.syncAliyunDomain());
    }

    @ApiOperation(value = "更新域名活跃状态")
    @PutMapping(value = "/active/set")
    public HttpResult<Boolean> setAliyunDomainActive(@RequestBody AliyunDomainParam.SetActive param) {
        return new HttpResult<>(aliyunDomainFacade.setAliyunDomainActive(param.getId()));
    }

}

