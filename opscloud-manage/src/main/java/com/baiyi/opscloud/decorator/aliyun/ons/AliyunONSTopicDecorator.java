package com.baiyi.opscloud.decorator.aliyun.ons;

import com.aliyuncs.ons.model.v20190214.OnsTraceGetResultResponse;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.vo.cloud.AliyunONSVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/25 5:22 下午
 * @Since 1.0
 */

@Component("AliyunONSTopicDecorator")
public class AliyunONSTopicDecorator {

    @Resource
    private OcServerService ocServerService;

    public List<AliyunONSVO.TopicMessageTraceMap> decoratorTraceMapVOList(OnsTraceGetResultResponse.TraceData data) {
        List<AliyunONSVO.TopicMessageTraceMap> traceMapList = Lists.newArrayListWithCapacity(data.getTraceList().size());
        data.getTraceList().forEach(x -> traceMapList.add(decoratorTraceMapVO(x)));
        return traceMapList;
    }

    public AliyunONSVO.TopicMessageTraceMap decoratorTraceMapVO(OnsTraceGetResultResponse.TraceData.TraceMapDo traceMapDo) {
        AliyunONSVO.TopicMessageTraceMap traceMap = BeanCopierUtils.copyProperties(traceMapDo, AliyunONSVO.TopicMessageTraceMap.class);
        if (traceMapDo.getPubTime() != null) {
            traceMap.setPubTimestamp(traceMapDo.getPubTime());
            traceMap.setPubTime(TimeUtils.dateToStr(new Date(traceMapDo.getPubTime())));
        }
        OcServer ocServer = ocServerService.queryOcServerByIp(traceMap.getBornHost());
        if (ocServer != null)
            traceMap.setBornHostname(ServerBaseFacade.acqServerName(ocServer));
        List<AliyunONSVO.TopicMessageSubMap> subMapList = Lists.newArrayListWithCapacity(traceMapDo.getSubList().size());
        traceMapDo.getSubList().forEach( x-> subMapList.add(decoratorSubMapVO(x)));
        traceMap.setSubList(subMapList);
        return traceMap;
    }

    private AliyunONSVO.TopicMessageSubMap decoratorSubMapVO(OnsTraceGetResultResponse.TraceData.TraceMapDo.SubMapDo subMapDo) {
        AliyunONSVO.TopicMessageSubMap subMap = BeanCopierUtils.copyProperties(subMapDo, AliyunONSVO.TopicMessageSubMap.class);
        List<AliyunONSVO.TopicMessageSubClient> subClientList = Lists.newArrayListWithCapacity(subMapDo.getClientList().size());
        subMapDo.getClientList().forEach(x -> subClientList.add(decoratorSubClientVO(x)));
        subMap.setClientList(subClientList);
        subMap.setCount(subMapDo.getFailCount() + subMapDo.getSuccessCount());
        return subMap;
    }

    private AliyunONSVO.TopicMessageSubClient decoratorSubClientVO(OnsTraceGetResultResponse.TraceData.TraceMapDo.SubMapDo.SubClientInfoDo subClientInfoDo) {
        AliyunONSVO.TopicMessageSubClient subClient = BeanCopierUtils.copyProperties(subClientInfoDo, AliyunONSVO.TopicMessageSubClient.class);
        OcServer ocServer = ocServerService.queryOcServerByIp(subClient.getClientHost());
        if (ocServer != null)
            subClient.setClientHostname(ServerBaseFacade.acqServerName(ocServer));
        if (subClient.getSubTime() != null)
            subClient.setSubTimeToStr(TimeUtils.dateToStr(new Date(subClient.getSubTime())));
        return subClient;
    }
}
