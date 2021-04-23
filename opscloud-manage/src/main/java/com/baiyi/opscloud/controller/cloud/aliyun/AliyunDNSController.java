package com.baiyi.opscloud.controller.cloud.aliyun;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDNSVO;
import com.baiyi.opscloud.facade.aliyun.AliyunDNSFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 4:31 下午
 * @Since 1.0
 */
@RestController
@RequestMapping("/aliyun/dns")
@Api(tags = "阿里云DNS管理")
public class AliyunDNSController {

    @Resource
    private AliyunDNSFacade aliyunDNSFacade;

    @ApiOperation(value = "同步所有被管理域名的DNS信息")
    @GetMapping(value = "/sync")
    public HttpResult<Boolean> syncAliyunDomainRecord() {
        return new HttpResult<>(aliyunDNSFacade.syncAliyunDomainRecord());
    }

    @ApiOperation(value = "同步单个域名的DNS信息")
    @GetMapping(value = "/domain/sync")
    public HttpResult<Boolean> syncAliyunDomainRecordByName(@RequestParam String domainName) {
        return new HttpResult<>(aliyunDNSFacade.syncAliyunDomainRecordByName(domainName));
    }

    @ApiOperation(value = "分页查询DNS")
    @PostMapping(value = "/page/query")
    public HttpResult<DataTable<AliyunDNSVO.Record>> queryAliyunDNSPage(@RequestBody AliyunDomainRecordParam.PageQuery pageQuery) {
        return new HttpResult<>(aliyunDNSFacade.queryAliyunDNSPage(pageQuery));
    }

    @ApiOperation(value = "新增DNS解析")
    @PostMapping(value = "/add")
    public HttpResult<Boolean> addAliyunDomainRecord(@RequestBody @Valid AliyunDomainRecordParam.AddDomainRecord param) {
        return new HttpResult<>(aliyunDNSFacade.addAliyunDomainRecord(param));
    }

    @ApiOperation(value = "更新DNS解析")
    @PutMapping(value = "/update")
    public HttpResult<Boolean> updateDomainRecord(@RequestBody @Valid AliyunDomainRecordParam.UpdateDomainRecord param) {
        return new HttpResult<>(aliyunDNSFacade.updateDomainRecord(param));
    }

    @ApiOperation(value = "更新DNS解析状态")
    @PutMapping(value = "/status/update")
    public HttpResult<Boolean> domainRecordStatusUpdate(@RequestBody @Valid AliyunDomainRecordParam.SetDomainRecordStatus param) {
        return new HttpResult<>(aliyunDNSFacade.domainRecordStatusUpdate(param));
    }

    @ApiOperation(value = "删除DNS解析")
    @DeleteMapping(value = "/del")
    public HttpResult<Boolean> delDomainRecord(@RequestParam String recordId) {
        return new HttpResult<>(aliyunDNSFacade.delDomainRecord(recordId));
    }

}

