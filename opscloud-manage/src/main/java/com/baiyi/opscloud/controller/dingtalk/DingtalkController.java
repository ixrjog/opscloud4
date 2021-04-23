package com.baiyi.opscloud.controller.dingtalk;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcDingtalkDept;
import com.baiyi.opscloud.domain.param.dingtalk.DingtalkParam;
import com.baiyi.opscloud.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.opscloud.facade.dingtalk.DingtalkDeptFacade;
import com.baiyi.opscloud.facade.dingtalk.DingtalkUserFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/15 3:41 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/dingtalk")
@Api(tags = "钉钉管理")
public class DingtalkController {

    @Resource
    private DingtalkUserFacade dingtalkUserFacade;

    @Resource
    private DingtalkDeptFacade dingtalkDeptFacade;

    @ApiOperation(value = "用户分页查询")
    @PostMapping(value = "/user/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DingtalkVO.User>> queryDingtalkUserPage(@RequestBody DingtalkParam.UserPageQuery pageQuery) {
        return new HttpResult<>(dingtalkUserFacade.queryDingtalkUserPage(pageQuery));
    }

    @ApiOperation(value = "用户同步")
    @GetMapping(value = "/user/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncUser() {
        return new HttpResult<>(dingtalkUserFacade.syncUser());
    }

    @ApiOperation(value = "用户绑定")
    @PostMapping(value = "/user/bind", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> bindOcUser(@RequestBody DingtalkParam.BindOcUser param) {
        return new HttpResult<>(dingtalkUserFacade.bindOcUser(param));
    }

    @ApiOperation(value = "用户更新")
    @PostMapping(value = "/user/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUser(@RequestBody DingtalkParam.GetUser param) {
        return new HttpResult<>(dingtalkUserFacade.updateUser(param));
    }

    @ApiOperation(value = "dept同步")
    @GetMapping(value = "/dept/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncDept(@RequestParam String uid) {
        return new HttpResult<>(dingtalkDeptFacade.syncDept(uid));
    }

    @ApiOperation(value = "dept树结构查询")
    @GetMapping(value = "/dept/tree/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DingtalkVO.DeptTree> queryDingtalkDeptTree(@RequestParam String uid) {
        return new HttpResult<>(dingtalkDeptFacade.queryDingtalkDeptTree(uid));
    }

    @ApiOperation(value = "dept树结构刷新")
    @GetMapping(value = "/dept/tree/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DingtalkVO.DeptTree> refreshDingtalkDeptTree(@RequestParam String uid) {
        return new HttpResult<>(dingtalkDeptFacade.refreshDingtalkDeptTree(uid));
    }

    @ApiOperation(value = "root dept查询")
    @GetMapping(value = "/dept/root/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcDingtalkDept>> refreshDingtalkDeptTree() {
        return new HttpResult<>(dingtalkDeptFacade.queryDingtalkRootDept());
    }

    @ApiOperation(value = "dingtalk部门id查询钉钉用户")
    @GetMapping(value = "/user/dept/id/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<DingtalkVO.User>> queryDingtalkUserByDingtalkDeptId(@RequestParam Integer deptId) {
        return new HttpResult<>(dingtalkUserFacade.queryDingtalkUserByDingtalkDeptId(deptId));
    }
}
