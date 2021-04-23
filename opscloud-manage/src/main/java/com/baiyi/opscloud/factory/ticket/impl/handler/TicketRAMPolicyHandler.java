package com.baiyi.opscloud.factory.ticket.impl.handler;

import com.baiyi.opscloud.cloud.ram.AliyunRAMUserCenter;
import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.common.util.PasswordUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.facade.aliyun.AliyunRAMFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.factory.ticket.entry.RAMPolicyEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/11 3:06 下午
 * @Version 1.0
 */
@Slf4j
@Component("TicketRAMPolicyExecutor")
public class TicketRAMPolicyHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private AliyunRAMUserCenter aliyunRAMUserCenter;

    @Resource
    private AliyunRAMFacade aliyunRAMFacade;

    @Resource
    private StringEncryptor stringEncryptor;

    @Override
    public String getKey() {
        return WorkorderKey.RAM_POLICY.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.RAM_POLICY.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, RAMPolicyEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        return (T) new GsonBuilder().create().fromJson(ocWorkorderTicketEntry.getEntryDetail(), RAMPolicyEntry.class);
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        OcUser ocUser = getUser(ocWorkorderTicketEntry.getWorkorderTicketId());
        RAMPolicyEntry ramPolicyEntry = (RAMPolicyEntry) entry;
        // 校验RAM账户
        BusinessWrapper<OcAliyunRamUser> createRamUserWrapper = createRamUser(ramPolicyEntry, ocUser);
        if (!createRamUserWrapper.isSuccess()) {
            // 创建RAM账户错误
            saveTicketEntry(ocWorkorderTicketEntry, new BusinessWrapper<>(createRamUserWrapper.getCode(), createRamUserWrapper.getDesc()));
            return;
        }
        // 授权
        OcAliyunRamUser ocAliyunRamUser = createRamUserWrapper.getBody();
        BusinessWrapper<Boolean> wrapper = aliyunRAMFacade.attachPolicyToUser(ocAliyunRamUser, ramPolicyEntry.getRamPolicy());
        // 更新数据
        if (wrapper.isSuccess()) {
            saveTicketEntry(ocWorkorderTicketEntry, BusinessWrapper.SUCCESS);
        } else {
            saveTicketEntry(ocWorkorderTicketEntry, new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc()));
        }
    }

    private BusinessWrapper<OcAliyunRamUser> createRamUser(RAMPolicyEntry ramPolicyEntry, OcUser ocUser) {
        if (!StringUtils.isEmpty(ocUser.getPassword())) {
            String password = stringEncryptor.decrypt(ocUser.getPassword());
            ocUser.setPassword(password);
        }else{
            /**
             * 设置密码强度
             * https://help.aliyun.com/document_detail/116413.html?spm=a2c4g.11186623.2.11.1b6432e8g8irrX#task-188785
             */
            ocUser.setPassword(PasswordUtils.getPW(32));
        }
        return aliyunRAMUserCenter.createRamUser(ramPolicyEntry.getRamPolicy().getAccountUid(), ocUser);
    }


    @Override
    protected BusinessWrapper<Boolean> updateTicketEntry(WorkorderTicketEntryVO.Entry entry) {
        return BusinessWrapper.SUCCESS;
    }

}