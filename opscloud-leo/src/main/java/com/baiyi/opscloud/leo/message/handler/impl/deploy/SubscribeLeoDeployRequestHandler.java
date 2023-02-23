package com.baiyi.opscloud.leo.message.handler.impl.deploy;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeployRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployVO;
import com.baiyi.opscloud.leo.message.handler.base.BaseLeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.leo.packer.LeoDeployResponsePacker;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/12/6 19:49
 * @Version 1.0
 */
@Slf4j
@Component
public class SubscribeLeoDeployRequestHandler extends BaseLeoContinuousDeliveryRequestHandler<SubscribeLeoDeployRequestParam> {

    @Resource
    private LeoDeployService leoDeployService;

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private LeoDeployResponsePacker leoDeployResponsePacker;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_DEPLOY.name();
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) {
        SubscribeLeoDeployRequestParam queryParam = toRequestParam(message);
        List<Integer> jobIds = leoJobService.queryJobWithApplicationIdAndEnvType(queryParam.getApplicationId(), queryParam.getEnvType())
                .stream()
                .map(LeoJob::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(jobIds)){
            return;
        }
        queryParam.setJobIds(jobIds);
        DataTable<LeoDeployVO.Deploy> dataTable = queryLeoDeployPage(queryParam);
        sendToSession(session, dataTable);
    }

    public DataTable<LeoDeployVO.Deploy> queryLeoDeployPage(SubscribeLeoDeployRequestParam pageQuery) {
        DataTable<LeoDeploy> table = leoDeployService.queryDeployPage(pageQuery);
        List<LeoDeployVO.Deploy> data = BeanCopierUtil.copyListProperties(table.getData(), LeoDeployVO.Deploy.class).stream()
                .peek(leoDeployResponsePacker::wrap)
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    private SubscribeLeoDeployRequestParam toRequestParam(String message) {
        return toLeoRequestParam(message, SubscribeLeoDeployRequestParam.class);
    }

}
