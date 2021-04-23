package com.baiyi.opscloud.decorator.cloud;

import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/11/30 4:50 下午
 * @Version 1.0
 */
@Slf4j
public class CloudServerDecorator {

    public static CloudServerVO.CloudServer decorator(CloudServerVO.CloudServer cloudServer) {
        if (cloudServer.getExpiredTime() != null) {
            try {
                cloudServer.setExpiresDays(TimeUtils.calculateDateDiff4Day(new Date(), cloudServer.getExpiredTime()));
            } catch (RuntimeException e) {
                log.error("过期时间：{}", cloudServer.getExpiredTime());
                cloudServer.setExpiresDays(-1);
            }
        }
        return cloudServer;
    }
}
