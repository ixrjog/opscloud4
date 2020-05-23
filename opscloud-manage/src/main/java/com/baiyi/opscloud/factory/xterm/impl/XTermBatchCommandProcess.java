package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import com.baiyi.opscloud.xterm.message.BatchCommandMessage;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Author baiyi
 * @Date 2020/5/11 7:22 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class XTermBatchCommandProcess extends BaseXTermProcess implements IXTermProcess {

    /**
     * XTerm设置批量命令
     * @return
     */

    @Override
    public String getKey() {
        return XTermRequestStatus.BATCH_COMMAND.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        BatchCommandMessage xtermMessage = (BatchCommandMessage) getXTermMessage(message);
        JSchSessionMap.setBatch(session.getId(), xtermMessage.getIsBatch());
    }

    @Override
    protected BaseMessage getXTermMessage(String message) {
        BatchCommandMessage xtermMessage = new GsonBuilder().create().fromJson(message, BatchCommandMessage.class);
        return xtermMessage;
    }

}
