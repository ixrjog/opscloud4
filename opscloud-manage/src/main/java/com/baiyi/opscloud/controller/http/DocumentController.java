package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.DocumentParam;
import com.baiyi.opscloud.domain.vo.sys.DocumentVO;
import com.baiyi.opscloud.facade.sys.DocumentFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2021/6/16 11:23 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sys/doc")
@Api(tags = "文档管理")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentFacade documentFacade;

    @ApiOperation(value = "查阅文档")
    @PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.Doc> previewDocument(@RequestBody @Valid DocumentParam.DocumentQuery query) {
        return new HttpResult<>(documentFacade.previewDocument(query));
    }

    @ApiOperation(value = "按挂载区查询文档")
    @PostMapping(value = "/zone/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DocumentVO.DocZone> getDocumentZone(@RequestBody @Valid DocumentParam.DocumentZoneQuery query) {
        return new HttpResult<>(documentFacade.getDocZone(query));
    }

    @ApiOperation(value = "分页查询文档")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DocumentVO.Document>> queryDocumentPage(@RequestBody @Valid DocumentParam.DocumentPageQuery query) {
        return new HttpResult<>(documentFacade.queryDocumentPage(query));
    }

    @ApiOperation(value = "新增文档")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDocument(@RequestBody @Valid DocumentParam.AddDocument addDocument) {
        documentFacade.addDocument(addDocument);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "删除指定的文档")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteDocumentById(@RequestParam int id) {
        documentFacade.deleteDocumentById(id);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "更新文档")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDocument(@RequestBody @Valid DocumentParam.UpdateDocument updateDocument) {
        documentFacade.updateDocument(updateDocument);
        return HttpResult.SUCCESS;
    }

    @ApiOperation(value = "分页查询文档挂载区")
    @PostMapping(value = "/zone/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DocumentVO.Zone>> queryDocumentZonePage(@RequestBody @Valid DocumentParam.DocumentZonePageQuery query) {
        return new HttpResult<>(documentFacade.queryDocumentZonePage(query));
    }

    @ApiOperation(value = "更新文档挂载区")
    @PutMapping(value = "/zone/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateDocumentZone(@RequestBody @Valid DocumentParam.UpdateDocumentZone updateDocumentZone) {
        documentFacade.updateDocumentZone(updateDocumentZone);
        return HttpResult.SUCCESS;
    }

}
