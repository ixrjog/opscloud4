package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.org.DepartmentMemberParam;
import com.baiyi.opscloud.domain.param.org.DepartmentParam;
import com.baiyi.opscloud.domain.vo.org.DepartmentTreeVO;
import com.baiyi.opscloud.domain.vo.org.OrgDepartmentMemberVO;
import com.baiyi.opscloud.domain.vo.org.OrgDepartmentVO;
import com.baiyi.opscloud.domain.vo.org.OrgChartVO;
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

    @ApiOperation(value = "分页查询查询部门列表")
    @PostMapping(value = "/department/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OrgDepartmentVO.Department>> queryDepartmentPage(@RequestBody @Valid DepartmentParam.PageQuery pageQuery) {
        return new HttpResult<>(orgFacade.queryDepartmentPage(pageQuery));
    }

    @ApiOperation(value = "查询部门树")
    @GetMapping(value = "/department/tree/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DepartmentTreeVO.DepartmentTree> queryDepartmentTree(@Valid int parentId) {
        return new HttpResult<>(orgFacade.queryDepartmentTree(parentId));
    }

    @ApiOperation(value = "查询组织架构图")
    @GetMapping(value = "/chart/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OrgChartVO.OrgChart> queryOrgChartByParentId(@Valid int parentId) {
        return new HttpResult<>(orgFacade.queryOrgChart(parentId));
    }

    @ApiOperation(value = "组织架构部门drop")
    @GetMapping(value = "/department/tree/drop", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> dropDepartmentTree(@Valid int draggingParentId, int dropParentId, String dropType) {
        return new HttpResult<>(orgFacade.dropDepartmentTree(draggingParentId, dropParentId, dropType));
    }

    @ApiOperation(value = "新增部门")
    @PostMapping(value = "/department/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDepartment(@RequestBody @Valid OrgDepartmentVO.Department department) {
        return new HttpResult<>(orgFacade.addDepartment(department));
    }

    @ApiOperation(value = "更新部门")
    @PutMapping(value = "/department/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDepartment(@RequestBody @Valid OrgDepartmentVO.Department department) {
        return new HttpResult<>(orgFacade.updateDepartment(department));
    }

    @ApiOperation(value = "查询部门")
    @GetMapping(value = "/department/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OrgDepartmentVO.Department> queryDepartmentById(@Valid int id) {
        return new HttpResult<>(orgFacade.queryDepartmentById(id));
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping(value = "/department/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delDepartment(@RequestParam int id) {
        return new HttpResult<>(orgFacade.delDepartmentById(id));
    }

    @ApiOperation(value = "分页查询成员列表")
    @PostMapping(value = "/department/member/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OrgDepartmentMemberVO.DepartmentMember>> queryDepartmentMemberPage(@RequestBody @Valid DepartmentMemberParam.PageQuery pageQuery) {
        return new HttpResult<>(orgFacade.queryDepartmentMemberPage(pageQuery));
    }

    @ApiOperation(value = "添加部门成员")
    @PutMapping(value = "/department/member/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDepartmentMember(@Valid int departmentId, int userId) {
        return new HttpResult<>(orgFacade.addDepartmentMember(departmentId, userId));
    }

    @ApiOperation(value = "设置部门成员(当前用户加入部门)")
    @PutMapping(value = "/department/member/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> joinDepartmentMember(@Valid int departmentId) {
        return new HttpResult<>(orgFacade.joinDepartmentMember(departmentId));
    }

    @ApiOperation(value = "移除部门成员")
    @DeleteMapping(value = "/department/member/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> removeDepartmentMember(@RequestParam int id) {
        return new HttpResult<>(orgFacade.delDepartmentMemberById(id));
    }

    @ApiOperation(value = "更新部门成员经理属性")
    @PutMapping(value = "/department/member/leader/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDepartmentMemberLeader(@Valid int id) {
        return new HttpResult<>(orgFacade.updateDepartmentMemberLeader(id));
    }

    @ApiOperation(value = "更新部门成员审批权属性")
    @PutMapping(value = "/department/member/approval/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDepartmentMemberApproval(@Valid int id) {
        return new HttpResult<>(orgFacade.updateDepartmentMemberApprovalAuthority(id));
    }

    @ApiOperation(value = "校验用户是否加入部门")
    @GetMapping(value = "/department/user/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> checkUserInTheDepartment() {
        return new HttpResult<>(orgFacade.checkUserInTheDepartment());
    }

}
