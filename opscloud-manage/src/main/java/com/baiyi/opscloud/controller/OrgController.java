package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OcOrgDepartmentMemberVO;
import com.baiyi.opscloud.facade.OrgFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:26 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/org")
@Api(tags = "组织架构管理")
public class OrgController {

    @Resource
    private OrgFacade orgFacade;

    @ApiOperation(value = "查询部门树")
    @GetMapping(value = "/department/tree/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DepartmentTreeVO.DepartmentTree> queryDepartmentTree(@Valid int parentId) {
        return new HttpResult<>(orgFacade.queryDepartmentTree(parentId));
    }

    @ApiOperation(value = "组织架构部门drop")
    @GetMapping(value = "/department/tree/drop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> dropDepartmentTree(@Valid int draggingParentId, int dropParentId, String dropType) {
        return new HttpResult<>(orgFacade.dropDepartmentTree(draggingParentId, dropParentId, dropType));
    }

    @ApiOperation(value = "分页查询成员列表")
    @PostMapping(value = "/department/member/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcOrgDepartmentMemberVO.DepartmentMember>> queryDepartmentMemberPage(@RequestBody @Valid DepartmentMemberParam.PageQuery pageQuery) {
        return new HttpResult<>(orgFacade.queryDepartmentMemberPage(pageQuery));
    }

}
