package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.tag.OcBusinessTagVO;
import com.baiyi.opscloud.domain.vo.tag.OcTagVO;
import com.baiyi.opscloud.facade.TagFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:15 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "标签管理")
public class TagController {

    @Resource
    private TagFacade tagFacade;

    @ApiOperation(value = "分页查询tag列表")
    @GetMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcTagVO.Tag>> queryTagPage(@Valid TagParam.PageQuery pageQuery) {
        return new HttpResult<>(tagFacade.queryTagPage(pageQuery));
    }

    @ApiOperation(value = "新增tag")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTag(@RequestBody @Valid OcTagVO.Tag tag) {
        return new HttpResult<>(tagFacade.addTag(tag));
    }

    @ApiOperation(value = "更新tag")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTag(@RequestBody @Valid OcTagVO.Tag tag) {
        return new HttpResult<>(tagFacade.updateTag(tag));
    }

    @ApiOperation(value = "删除指定的tag")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTagById(@RequestParam int id) {
        return new HttpResult<>(tagFacade.deleteTagById(id));
    }

    @ApiOperation(value = "查询业务tag")
    @GetMapping(value = "/business/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcTagVO.Tag>> queryBusinessTag(@Valid TagParam.BusinessQuery businessQuery) {
        return new HttpResult<>(tagFacade.queryBusinessTag(businessQuery));
    }

    @ApiOperation(value = "查询业务未使用tag")
    @GetMapping(value = "/business/notin/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcTagVO.Tag>> queryNotInBusinessTag(@Valid TagParam.BusinessQuery businessQuery) {
        return new HttpResult<>(tagFacade.queryNotInBusinessTag(businessQuery));
    }

    @ApiOperation(value = "更新业务绑定的tag")
    @PutMapping(value = "/business/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTag(@RequestBody @Valid OcBusinessTagVO.BusinessTag businessTag) {
        return new HttpResult<>(tagFacade.updateBusinessTag(businessTag));
    }
}
