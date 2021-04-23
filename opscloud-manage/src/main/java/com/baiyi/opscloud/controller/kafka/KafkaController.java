package com.baiyi.opscloud.controller.kafka;

import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.domain.param.kafka.KafkaParam;
import com.baiyi.opscloud.domain.vo.kafka.KafkaVO;
import com.baiyi.opscloud.facade.kafka.KafkaGroupFacade;
import com.baiyi.opscloud.facade.kafka.KafkaTopicFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/18 1:37 下午
 * @Since 1.0
 */

@RestController
@RequestMapping("/kafka")
@Api(tags = "kafka管理")
public class KafkaController {

    @Resource
    private KafkaGroupFacade kafkaGroupFacade;

    @Resource
    private KafkaTopicFacade kafkaTopicFacade;

    @ApiOperation(value = "kafkaTopic查询")
    @PostMapping(value = "/topic/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KafkaVO.Topic> queryOcItAssetPage(@RequestBody @Valid KafkaParam.TopicQuery param) {
        return new HttpResult<>(kafkaTopicFacade.kafkaTopicQuery(param));
    }

    @ApiOperation(value = "kafkaGroup查询")
    @PostMapping(value = "/group/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KafkaVO.Group> queryOcItAssetPage(@RequestBody @Valid KafkaParam.GroupQuery param) {
        return new HttpResult<>(kafkaGroupFacade.kafkaGroupQuery(param));
    }

    @ApiOperation(value = "kafkaTopic检验")
    @GetMapping(value = "/topic/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> kafkaTopicCheck(@RequestParam String topic) {
        return new HttpResult<>(kafkaTopicFacade.kafkaTopicCheck(topic));
    }

    @ApiOperation(value = "kafkaGroup检验")
    @GetMapping(value = "/group/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> kafkaGroupCheck(@RequestParam String consumerId) {
        return new HttpResult<>(kafkaGroupFacade.kafkaGroupCheck(consumerId));
    }

}
