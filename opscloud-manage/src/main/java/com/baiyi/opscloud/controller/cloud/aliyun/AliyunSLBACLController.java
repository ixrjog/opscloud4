package com.baiyi.opscloud.controller.cloud.aliyun;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.baiyi.opscloud.facade.aliyun.AliyunSLBACLFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:47 下午
 * @Since 1.0
 */
@RestController
@RequestMapping("/aliyun/slb/acl")
@Api(tags = "阿里云SLB访问控制管理")
public class AliyunSLBACLController {

    @Resource
    private AliyunSLBACLFacade aliyunSLBACLFacade;

    @ApiOperation(value = "分页查询SLB访问控制")
    @PostMapping(value = "/page/query")
    public HttpResult<DataTable<AliyunSLBVO.AccessControl>> queryAccessControlPage(
            @RequestBody AliyunSLBParam.ACLPageQuery pageQuery) {
        return new HttpResult<>(aliyunSLBACLFacade.queryAccessControlPage(pageQuery));
    }

    @ApiOperation(value = "同步SLB访问控制")
    @GetMapping(value = "/sync")
    public HttpResult<Boolean> syncSLBACL() {
        return new HttpResult<>(aliyunSLBACLFacade.syncSLBACL());
    }

    @ApiOperation(value = "查询SLB访问控制条目")
    @GetMapping(value = "/entry/query")
    public HttpResult<List<AliyunSLBVO.AccessControlEntry>> queryAccessControlEntry(String slbAclId) {
        return new HttpResult<>(aliyunSLBACLFacade.queryAccessControlEntry(slbAclId));
    }

}
