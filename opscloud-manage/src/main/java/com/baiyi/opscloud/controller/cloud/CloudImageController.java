package com.baiyi.opscloud.controller.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.cloud.CloudImageParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudImageVO;
import com.baiyi.opscloud.facade.CloudImageFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/3/18 11:40 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/cloud/image")
@Api(tags = "云镜像")
public class CloudImageController {

    @Resource
    private CloudImageFacade cloudImageFacade;

    @ApiOperation(value = "分页模糊查询云镜像列表")
    @PostMapping(value = "/page/fuzzy/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CloudImageVO.CloudImage>> queryCloudImagePage(@RequestBody @Valid CloudImageParam.PageQuery pageQuery) {
        return new HttpResult<>(cloudImageFacade.fuzzyQueryCloudImagePage(pageQuery));
    }

    @ApiOperation(value = "同步指定的云镜像")
    @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncCloudImageByKey(@RequestParam String key) {
        return new HttpResult<>(cloudImageFacade.syncCloudImageByKey(key));
    }

    @ApiOperation(value = "删除指定的云镜像")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCloudImageById(@RequestParam int id) {
        return new HttpResult<>(cloudImageFacade.deleteCloudImageById(id));
    }

    @ApiOperation(value = "设置云镜像是否有效")
    @PutMapping(value = "/active/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCloudImageActive(@RequestParam int id) {
        return new HttpResult<>(cloudImageFacade.setCloudImageActive(id));
    }

}
