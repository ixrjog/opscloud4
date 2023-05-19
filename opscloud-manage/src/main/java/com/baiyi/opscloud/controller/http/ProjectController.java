package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;
import com.baiyi.opscloud.facade.project.ProjectFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2023/5/19 10:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/project")
@Tag(name = "项目管理")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectFacade projectFacade;

    @Operation(summary = "分页查询Leo任务关联的项目")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ProjectVO.Project>> queryLeoJobProjectPage(@RequestBody @Valid ProjectParam.ResProjectPageQuery pageQuery) {
        return new HttpResult<>(projectFacade.queryResProjectPage(pageQuery));
    }

}
