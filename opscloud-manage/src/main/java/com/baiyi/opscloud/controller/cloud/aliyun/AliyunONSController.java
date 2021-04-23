package com.baiyi.opscloud.controller.cloud.aliyun;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.param.cloud.AliyunONSParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.baiyi.opscloud.facade.aliyun.AliyunONSFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 4:07 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/aliyun/ons")
@Api(tags = "阿里云ONS管理")
public class AliyunONSController {

    @Resource
    private AliyunONSFacade aliyunONSFacade;

    @ApiOperation(value = "ons所有实例查询")
    @GetMapping(value = "/instance/all/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<OcAliyunOnsInstance>> queryONSInstanceAll() {
        return new HttpResult<>(aliyunONSFacade.queryONSInstanceAll());
    }

    @ApiOperation(value = "ons实例列表查询")
    @GetMapping(value = "/instance/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AliyunONSVO.Instance>> queryONSInstanceList(@RequestParam String regionId) {
        return new HttpResult<>(aliyunONSFacade.queryONSInstanceList(regionId));
    }

    @ApiOperation(value = "ons实例信息统计")
    @GetMapping(value = "/instance/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.InstanceStatistics> onsInstanceStatistics() {
        return new HttpResult<>(aliyunONSFacade.onsInstanceStatistics());
    }

    @ApiOperation(value = "ons实例同步")
    @GetMapping(value = "/instance/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncONSInstance(@RequestParam String regionId) {
        return new HttpResult<>(aliyunONSFacade.syncONSInstance(regionId));
    }

    @ApiOperation(value = "刷新ons实例详情")
    @PostMapping(value = "/instance/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> refreshONSInstanceDetail(@RequestBody @Valid AliyunONSParam.QueryInstanceDetail param) {
        return new HttpResult<>(aliyunONSFacade.refreshONSInstanceDetail(param));
    }

    @ApiOperation(value = "onsTopic同步")
    @PostMapping(value = "/topic/sync", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncONSTopic(@RequestBody @Valid AliyunONSParam.QueryTopicList param) {
        return new HttpResult<>(aliyunONSFacade.syncONSTopic(param));
    }

    @ApiOperation(value = "分页查询ONSTopic")
    @PostMapping(value = "/topic/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunONSVO.Topic>> queryONSTopicPage(@RequestBody @Valid AliyunONSParam.TopicPageQuery pageQuery) {
        return new HttpResult<>(aliyunONSFacade.queryONSTopicPage(pageQuery));
    }

    @ApiOperation(value = "查询ONSTopic订阅详情")
    @PostMapping(value = "/topic/sub/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.TopicSubDetail> queryOnsTopicSubDetail(@RequestBody @Valid AliyunONSParam.QueryTopicSubDetail param) {
        return new HttpResult<>(aliyunONSFacade.queryOnsTopicSubDetail(param));
    }

    @ApiOperation(value = "创建ONSTopic")
    @PostMapping(value = "/topic/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> onsTopicCreate(@RequestBody @Valid AliyunONSParam.TopicCreate param) {
        return new HttpResult<>(aliyunONSFacade.onsTopicCreate(param));
    }

    @ApiOperation(value = "onsGroup同步")
    @PostMapping(value = "/group/sync", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> syncONSGroup(@RequestBody @Valid AliyunONSParam.QueryGroupList param) {
        return new HttpResult<>(aliyunONSFacade.syncONSGroup(param));
    }

    @ApiOperation(value = "校验ONSTopic命名")
    @PostMapping(value = "/topic/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> onsTopicCheck(@RequestBody @Valid AliyunONSParam.TopicCheck param) {
        return new HttpResult<>(aliyunONSFacade.onsTopicCheck(param));
    }

    @ApiOperation(value = "校验ONSTopic命名V2")
    @GetMapping(value = "/topic/checkV2", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> onsTopicCheckV2(@RequestParam String topic) {
        return new HttpResult<>(aliyunONSFacade.onsTopicCheckV2(topic));
    }

    @ApiOperation(value = "分页查询ONSGroup")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AliyunONSVO.Group>> queryONSGroupPage(@RequestBody @Valid AliyunONSParam.GroupPageQuery pageQuery) {
        return new HttpResult<>(aliyunONSFacade.queryONSGroupPage(pageQuery));
    }

    @ApiOperation(value = "查询ONSGroup订阅详情")
    @PostMapping(value = "/group/sub/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.GroupSubDetail> queryOnsGroupSubDetail(@RequestBody @Valid AliyunONSParam.QueryGroupSubDetail param) {
        return new HttpResult<>(aliyunONSFacade.queryOnsGroupSubDetail(param));
    }

    @ApiOperation(value = "创建ONSGroup")
    @PostMapping(value = "/group/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> onsGroupCreate(@RequestBody @Valid AliyunONSParam.GroupCreate param) {
        return new HttpResult<>(aliyunONSFacade.onsGroupCreate(param));
    }

    @ApiOperation(value = "校验ONSGroup命名")
    @PostMapping(value = "/group/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> queryOnsGroupSubDetail(@RequestBody @Valid AliyunONSParam.GroupCheck param) {
        return new HttpResult<>(aliyunONSFacade.onsGroupCheck(param));
    }

    @ApiOperation(value = "校验ONSGroup命名V2")
    @GetMapping(value = "/group/checkV2", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> onsGroupCheckV2(@RequestParam String groupId) {
        return new HttpResult<>(aliyunONSFacade.onsGroupCheckV2(groupId));
    }

    @ApiOperation(value = "查询消费者状态")
    @PostMapping(value = "/group/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.GroupStatus> onsGroupStatus(@RequestBody @Valid AliyunONSParam.QueryGroupSubDetail param) {
        return new HttpResult<>(aliyunONSFacade.onsGroupStatus(param));
    }

    @ApiOperation(value = "保存消费者告警配置")
    @PostMapping(value = "/group/alarm/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveONSGroupAlarm(@RequestBody @Valid AliyunONSVO.GroupAlarm groupAlarm) {
        return new HttpResult<>(aliyunONSFacade.saveONSGroupAlarm(groupAlarm));
    }

    @ApiOperation(value = "查询消费者告警配置")
    @GetMapping(value = "/group/alarm/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.GroupAlarm> queryONSGroupAlarm(@RequestParam Integer onsGroupId) {
        return new HttpResult<>(aliyunONSFacade.queryONSGroupAlarm(onsGroupId));
    }

    @ApiOperation(value = "Topic消息分页查询")
    @PostMapping(value = "/topic/msg/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<AliyunONSVO.TopicMessage>> onsMessagePageQuery(@RequestBody @Valid AliyunONSParam.QueryTopicSubDetail param) {
        return new HttpResult<>(aliyunONSFacade.onsMessagePageQuery(param));
    }

    @ApiOperation(value = "Topic消息查询")
    @PostMapping(value = "/topic/msg/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.TopicMessage> queryOnsMessage(@RequestBody @Valid AliyunONSParam.QueryTopicMsg param) {
        return new HttpResult<>(aliyunONSFacade.queryOnsMessage(param));
    }

    @ApiOperation(value = "Topic消息轨迹消除")
    @PostMapping(value = "/topic/msg/trace/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.TopicMessageTraceMap> queryOnsTrace(@RequestBody @Valid AliyunONSParam.QueryTrace param) {
        return new HttpResult<>(aliyunONSFacade.queryOnsTrace(param));
    }

    @ApiOperation(value = "查询Topic所在的实例")
    @GetMapping(value = "/instance/topic/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.applyInstance> queryOnsInstanceByTopic(@RequestParam String topic) {
        return new HttpResult<>(aliyunONSFacade.queryOnsInstanceByTopic(topic));
    }

    @ApiOperation(value = "查询Group所在的实例")
    @GetMapping(value = "/instance/group/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AliyunONSVO.applyInstance> queryOcInstanceByGroupId(@RequestParam String groupId) {
        return new HttpResult<>(aliyunONSFacade.queryOcInstanceByGroupId(groupId));
    }
}
