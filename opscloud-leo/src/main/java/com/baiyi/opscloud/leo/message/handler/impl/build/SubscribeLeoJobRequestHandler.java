package com.baiyi.opscloud.leo.message.handler.impl.build;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoJobRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.leo.message.handler.base.BaseLeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.leo.packer.LeoJobResponsePacker;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/23 16:10
 * @Version 1.0
 */
@Slf4j
@Component
public class SubscribeLeoJobRequestHandler extends BaseLeoContinuousDeliveryRequestHandler<SubscribeLeoJobRequestParam> {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private LeoJobResponsePacker leoJobResponsePacker;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_JOB.name();
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) throws IOException {
        SubscribeLeoJobRequestParam queryParam = toRequestParam(message);
        DataTable<LeoJobVO.Job> dataTable = queryLeoJobPage(queryParam);
        sendToSession(session, dataTable);
    }

    public DataTable<LeoJobVO.Job> queryLeoJobPage(SubscribeLeoJobRequestParam pageQuery) {
        DataTable<LeoJob> table = leoJobService.queryJobPage(pageQuery);
        List<LeoJobVO.Job> data = BeanCopierUtil.copyListProperties(table.getData(), LeoJobVO.Job.class).stream()
                .peek(leoJobResponsePacker::wrap)
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    private SubscribeLeoJobRequestParam toRequestParam(String message) {
        return toLeoRequestParam(message, SubscribeLeoJobRequestParam.class);
    }

}