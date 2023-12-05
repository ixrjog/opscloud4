package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.tag.BusinessTagParam;
import com.baiyi.opscloud.domain.param.tag.TagParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.util.OptionsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 2:32 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/tag")
@Tag(name = "标签管理")
@RequiredArgsConstructor
public class TagController {

    private final SimpleTagFacade tagFacade;

    @Operation(summary = "查询业务类型选项")
    @GetMapping(value = "/business/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getBusinessTypeOptions() {
        return new HttpResult<>(OptionsUtil.toBusinessTypeOptions());
    }

    @Operation(summary = "更新业务标签")
    @PutMapping(value = "/business/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessTags(@RequestBody @Valid BusinessTagParam.UpdateBusinessTags updateBusinessTags) {
        tagFacade.updateBusinessTags(updateBusinessTags);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "按类型查询所有标签")
    @GetMapping(value = "/business/type/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TagVO.Tag>> getTagByBusinessType(@RequestParam @Valid int businessType) {
        return new HttpResult<>(tagFacade.queryTagByBusinessType(businessType));
    }

    @Operation(summary = "分页查询标签列表")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TagVO.Tag>> queryTagPage(@RequestBody @Valid TagParam.TagPageQuery pageQuery) {
        return new HttpResult<>(tagFacade.queryTagPage(pageQuery));
    }

    @Operation(summary = "新增标签信息")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTag(@RequestBody @Valid TagParam.Tag tag) {
        tagFacade.addTag(tag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "更新标签信息")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTag(@RequestBody @Valid TagParam.Tag tag) {
        tagFacade.updateTag(tag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "删除指定的标签信息")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTagById(@RequestParam @Valid int id) {
        tagFacade.deleteTagById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "查询FinOps标签选项")
    @GetMapping(value = "/finops/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getFinOpsTagOptions() {
        return new HttpResult<>(OptionsUtil.toFinOpsTagOptions(tagFacade.getFinOpsTags()));
    }

}