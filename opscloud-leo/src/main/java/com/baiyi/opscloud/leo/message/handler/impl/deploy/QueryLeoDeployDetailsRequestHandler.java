package com.baiyi.opscloud.leo.message.handler.impl.deploy;

import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.domain.param.leo.request.QueryLeoDeployDetailsRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.message.handler.base.BaseLeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.leo.util.SnapshotStash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2022/12/6 19:34
 * @Version 1.0
 */
@Slf4j
@Component
public class QueryLeoDeployDetailsRequestHandler extends BaseLeoContinuousDeliveryRequestHandler<QueryLeoDeployDetailsRequestParam> {

    @Resource
    private SnapshotStash snapshotStash;

    @Override
    public String getMessageType() {
        return LeoRequestType.QUERY_LEO_DEPLOY_DETAILS.name();
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) {
        QueryLeoDeployDetailsRequestParam param = toRequestParam(message);
        LeoContinuousDeliveryResponse response;
        if (snapshotStash.isExist(param.getDeployId())) {
            LeoDeployingVO.Deploying deploying = snapshotStash.get(param.getDeployId());
            response = LeoContinuousDeliveryResponse.builder()
                    .body(deploying)
                    .messageType(getMessageType())
                    .build();
        } else {
            response = LeoContinuousDeliveryResponse.builder()
                    .body(LeoDeployingVO.Deploying.builder().build())
                    .messageType(getMessageType())
                    .build();
        }
        sendToSession(session, response);
    }

    private QueryLeoDeployDetailsRequestParam toRequestParam(String message) {
        return toLeoRequestParam(message, QueryLeoDeployDetailsRequestParam.class);
    }

}
