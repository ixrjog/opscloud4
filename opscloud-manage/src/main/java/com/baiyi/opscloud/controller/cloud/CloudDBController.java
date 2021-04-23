package com.baiyi.opscloud.controller.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.param.cloud.CloudDBParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBAccountVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBVO;
import com.baiyi.opscloud.facade.CloudDBFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/3/1 11:15 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/db")
@Api(tags = "云数据库")
public class CloudDBController {

    @Resource
    private CloudDBFacade cloudDBFacade;

    @ApiOperation(value = "分页查询云数据库实例列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudDBVO.CloudDB>> fuzzyQueryCloudDBPage(@RequestBody @Valid CloudDBParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudDBFacade.fuzzyQueryCloudDBPage(pageQuery));
    }

    @ApiOperation(value = "删除指定的云数据库实例")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudDBById(@RequestParam int id) {
        return new HttpResult<>(cloudDBFacade.deleteCloudDBById(id));
    }

    @ApiOperation(value = "同步云数据库实例")
    @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudDBByKey() {
        return new HttpResult<>(cloudDBFacade.syncCloudDB());
    }

    @ApiOperation(value = "云数据库实例账户授权")
    @PutMapping(value = "/account/privilege", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> privilegeCloudDBAccount(@RequestBody @Valid CloudDBAccountVO.PrivilegeAccount privilegeAccount) {
        return new HttpResult<>(cloudDBFacade.privilegeAccount(privilegeAccount));
    }

    @ApiOperation(value = "同步云数据库实例")
    @GetMapping(value = "/database/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudDBByKey(@RequestParam int id) {
        return new HttpResult<>(cloudDBFacade.syncCloudDatabase(id));
    }

    @ApiOperation(value = "分页查询云数据库列表")
    @PostMapping(value = "/database/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudDBDatabaseVO.CloudDBDatabase>> fuzzyQueryCloudDBDatabasePage(@RequestBody @Valid CloudDBDatabaseParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudDBFacade.fuzzyQueryCloudDBDatabasePage(pageQuery));
    }

    @ApiOperation(value = "更新云数据库信息")
    @PutMapping(value = "/database/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateCloudDBDatabase(@RequestBody @Valid CloudDBDatabaseVO.CloudDBDatabase cloudDBDatabase) {
        return new HttpResult<>(cloudDBFacade.updateBaseCloudDBDatabase(cloudDBDatabase));
    }

    @ApiOperation(value = "分页查询云数据库SlowLogs")
    @PostMapping(value = "/database/slowlog/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudDatabaseSlowLogVO.SlowLog>> queryCloudDBDatabaseSlowLogPage(@RequestBody @Valid CloudDBDatabaseParam.SlowLogPageQuery pageQuery) {
        return new HttpResult<>(cloudDBFacade.queryCloudDBDatabaseSlowLogPage(pageQuery));
    }

}
