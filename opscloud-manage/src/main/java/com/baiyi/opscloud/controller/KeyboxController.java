package com.baiyi.opscloud.controller;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.keybox.KeyboxParam;
import com.baiyi.opscloud.domain.vo.keybox.KeyboxVO;
import com.baiyi.opscloud.facade.KeyboxFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author baiyi
 * @Date 2020/5/21 10:40 上午
 * @Version 1.0
 */
@RestController
@RequestMapping("/keybox")
@Api(tags = "全局密钥配置(SSHKey)管理")
public class KeyboxController {

    @Resource
    private KeyboxFacade keyboxFacade;

    @ApiOperation(value = "分页查询全局密钥配置")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<KeyboxVO.Keybox>> queryKeyboxPage(@RequestBody @Valid KeyboxParam.PageQuery pageQuery) {
        return new HttpResult<>(keyboxFacade.queryKeyboxPage(pageQuery));
    }

    @ApiOperation(value = "新增全局密钥配置")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addKeybox(@RequestBody @Valid KeyboxVO.Keybox keybox) {
        return new HttpResult<>(keyboxFacade.addKeybox(keybox));
    }

    @ApiOperation(value = "删除指定全局密钥配置")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteKeyboxById(@RequestParam int id) {
        return new HttpResult<>(keyboxFacade.deleteKeyboxById(id));
    }
}
