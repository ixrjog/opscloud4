package com.baiyi.opscloud.controller.announcement;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcAnnouncement;
import com.baiyi.opscloud.domain.param.announcement.AnnouncementParam;
import com.baiyi.opscloud.facade.announcement.AnnouncementFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/25 2:33 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/anc")
@Api(tags = "公告管理")
public class AnnouncementController {

    @Resource
    private AnnouncementFacade announcementFacade;

    @ApiOperation(value = "公告分页查询")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<OcAnnouncement>> queryOcAnnouncementPage(@RequestBody AnnouncementParam.PageQuery pageQuery) {
        return new HttpResult<>(announcementFacade.queryOcAnnouncementPage(pageQuery));
    }

    @ApiOperation(value = "公告保存")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveAnnouncement(@RequestBody OcAnnouncement ocAnnouncement) {
        return new HttpResult<>(announcementFacade.saveAnnouncement(ocAnnouncement));
    }

    @ApiOperation(value = "公告删除")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> delAnnouncement(@RequestParam Integer id) {
        return new HttpResult<>(announcementFacade.delAnnouncement(id));
    }

    @ApiOperation(value = "有效公告查询")
    @GetMapping(value = "/valid/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcAnnouncement>> queryValidOcAnnouncement() {
        return new HttpResult<>(announcementFacade.queryValidOcAnnouncement());
    }
}
