package com.baiyi.opscloud.controller.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCVSwitchParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCSecurityGroupVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVSwitchVO;
import com.baiyi.opscloud.facade.CloudVPCFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/3/19 2:03 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/vpc")
@Api(tags = "云专有网络")
public class CloudVPCController {

    @Resource
    private CloudVPCFacade cloudVPCFacade;

    @ApiOperation(value = "分页模糊查询云VPC列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudVPCVO.CloudVpc>> fuzzyQueryCloudVPCPage(@RequestBody @Valid CloudVPCParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudVPCFacade.fuzzyQueryCloudVPCPage(pageQuery));
    }

    @ApiOperation(value = "分页查询云VPC列表(按可用区过滤)")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudVPCVO.CloudVpc>> queryCloudVPCPage(@RequestBody @Valid CloudVPCParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudVPCFacade.queryCloudVPCPage(pageQuery));
    }

    @ApiOperation(value = "同步指定的VPC")
    @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudVPCByKey(@RequestParam String key) {
        return new HttpResult<>(cloudVPCFacade.syncCloudVPCByKey(key));
    }

    @ApiOperation(value = "删除指定的VPC")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudVPCById(@RequestParam int id) {
        return new HttpResult<>(cloudVPCFacade.deleteCloudVPCById(id));
    }

    @ApiOperation(value = "设置VPC是否有效")
    @PutMapping(value = "/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCloudVPCActive(@RequestParam int id) {
        return new HttpResult<>(cloudVPCFacade.setCloudVPCActive(id));
    }

    // security_group
    @ApiOperation(value = "分页查询云VPC安全组列表")
    @PostMapping(value = "/security/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudVPCSecurityGroupVO.SecurityGroup>> queryCloudVPCSecurityGroupPage(@RequestBody @Valid CloudVPCSecurityGroupParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudVPCFacade.queryCloudVPCSecurityGroupPage(pageQuery));
    }

    @ApiOperation(value = "设置VPC安全组是否有效")
    @PutMapping(value = "/security/group/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCloudVPCSecurityGroupActive(@RequestParam int id) {
        return new HttpResult<>(cloudVPCFacade.setCloudVPCSecurityGroupActive(id));
    }

    @ApiOperation(value = "分页查询云VPC虚拟交换机列表")
    @PostMapping(value = "/vswitch/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudVSwitchVO.VSwitch>> queryCloudVPCVSwitchPage(@RequestBody @Valid CloudVPCVSwitchParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudVPCFacade.queryCloudVPCVSwitchPage(pageQuery));
    }

    @ApiOperation(value = "设置VPC虚拟交换机是否有效")
    @PutMapping(value = "/vswitch/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCloudVPCVSwitchActive(@RequestParam int id) {
        return new HttpResult<>(cloudVPCFacade.setCloudVPCVSwitchActive(id));
    }
}
