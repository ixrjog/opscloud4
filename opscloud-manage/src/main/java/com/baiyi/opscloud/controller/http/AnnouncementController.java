package com.baiyi.opscloud.controller.http;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.vo.sys.AnnouncementVO;
import com.baiyi.opscloud.facade.sys.AnnouncementFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/28 17:16
 * @Version 1.0
 */
@RestController
@RequestMapping( "/api/sys/announcement")
@Tag(name = "公告管理")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementFacade announcementFacade;

    @Operation(summary = "分类查询公告")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AnnouncementVO.Announcement>> getAnnouncement(@RequestParam @Valid int kind) {
        return new HttpResult<>(announcementFacade.getAnnouncementByKind(kind));
    }

}