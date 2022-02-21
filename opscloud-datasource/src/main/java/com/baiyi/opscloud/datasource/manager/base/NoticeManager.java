package com.baiyi.opscloud.datasource.manager.base;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.datasource.message.notice.NoticeHelper;
import com.baiyi.opscloud.domain.constants.DsInstanceTagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

/**
 * @Author baiyi
 * @Date 2021/12/2 11:02 AM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeManager {

    private final InstanceHelper instanceHelper;

    public interface MsgKeys {
        String CREATE_USER = "CREATE_USER";
        String UPDATE_USER_PASSWORD = "UPDATE_USER_PASSWORD";
        String CREATE_RAM_USER = "CREATE_RAM_USER";
        String CREATE_IAM_USER = "CREATE_IAM_USER";
        String TICKET_APPROVE = "TICKET_APPROVE";
        String TICKET_END = "TICKET_END";
    }

    /**
     * 支持通知的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.DINGTALK, DsTypeEnum.DINGTALK_APP};

    @Resource
    private NoticeHelper noticeHelper;

    /**
     * 简单消息发送
     *
     * @param user
     * @param msgKey
     */
    public void sendMessage(User user, String msgKey) {
        List<DatasourceInstance> instances = instanceHelper.listInstance(FILTER_INSTANCE_TYPES,
                DsInstanceTagConstants.NOTICE.getTag());
        if (!CollectionUtils.isEmpty(instances)) {
            noticeHelper.sendMessage(user, msgKey, instances);
        }
    }

    /**
     * 模版消息发送
     *
     * @param user
     * @param msgKey
     * @param iNoticeMessage
     */
    @Async(value = CORE)
    public void sendMessage(User user, String msgKey, INoticeMessage iNoticeMessage) {
        try {
            List<DatasourceInstance> instances = instanceHelper.listInstance(FILTER_INSTANCE_TYPES,
                    DsInstanceTagConstants.NOTICE.getTag());
            if (!CollectionUtils.isEmpty(instances)) {
                noticeHelper.sendMessage(user, msgKey, instances, iNoticeMessage);
            }
        } catch (Exception e) {
            log.error("发送消息失败: " + e.getMessage());
        }
    }


}
