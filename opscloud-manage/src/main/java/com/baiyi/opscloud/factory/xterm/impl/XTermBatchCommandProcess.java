package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.XTermRequestStatus;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.message.XTermBatchCommandMSMessage;
import com.baiyi.opscloud.xterm.model.JSchSessionMap;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 7:22 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class XTermBatchCommandProcess extends BaseXTermProcess implements IXTermProcess {


    @Override
    public String getKey() {
        return XTermRequestStatus.BATCH_COMMAND.getCode();
    }

    @Override
    public void xtermProcess(String message, Session session) {
        XTermBatchCommandMSMessage batchCmdMessage = (XTermBatchCommandMSMessage) getXTermMessage(message);
        JSchSessionMap.setBatch(session.getId(), batchCmdMessage.getIsBatch());
    }

    @Override
    protected BaseXTermWSMessage getXTermMessage(String message) {
        XTermBatchCommandMSMessage cmdMessage = new GsonBuilder().create().fromJson(message, XTermBatchCommandMSMessage.class);
        return cmdMessage;
    }


    /**
     * Maps key press events to the ascii values
     */
    static Map<Integer, byte[]> keyMap = new HashMap<>();

    static {
        //ESC
        keyMap.put(27, new byte[]{(byte) 0x1b});
        //ENTER
        keyMap.put(13, new byte[]{(byte) 0x0d});
        //LEFT
        keyMap.put(37, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x44});
        //UP
        keyMap.put(38, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x41});
        //RIGHT
        keyMap.put(39, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x43});
        //DOWN
        keyMap.put(40, new byte[]{(byte) 0x1b, (byte) 0x4f, (byte) 0x42});
        //BS
        keyMap.put(8, new byte[]{(byte) 0x7f});
        //TAB
        keyMap.put(9, new byte[]{(byte) 0x09});
        //CTR
        keyMap.put(17, new byte[]{});
        //DEL
        keyMap.put(46, "\033[3~".getBytes());
//        //CTR-A
//        keyMap.put(65, new byte[]{(byte) 0x01});
//        //CTR-B
//        keyMap.put(66, new byte[]{(byte) 0x02});
//        //CTR-C
//        keyMap.put(67, new byte[]{(byte) 0x03});
//        //CTR-D
//       //   keyMap.put(68, new byte[]{(byte) 0x04});
//        //CTR-E
//        keyMap.put(69, new byte[]{(byte) 0x05});
//        //CTR-F
//        keyMap.put(70, new byte[]{(byte) 0x06});
//        //CTR-G
//        keyMap.put(71, new byte[]{(byte) 0x07});
//        //CTR-H
//        keyMap.put(72, new byte[]{(byte) 0x08});
//        //CTR-I
//        keyMap.put(73, new byte[]{(byte) 0x09});
//        //CTR-J
//        keyMap.put(74, new byte[]{(byte) 0x0A});
//        //CTR-K
//        keyMap.put(75, new byte[]{(byte) 0x0B});
//        //CTR-L
//        keyMap.put(76, new byte[]{(byte) 0x0C});
//        //CTR-M
//        keyMap.put(77, new byte[]{(byte) 0x0D});
//        //CTR-N
//        keyMap.put(78, new byte[]{(byte) 0x0E});
//        //CTR-O
//        keyMap.put(79, new byte[]{(byte) 0x0F});
//        //CTR-P
//        keyMap.put(80, new byte[]{(byte) 0x10});
//        //CTR-Q
//       // keyMap.put(81, new byte[]{(byte) 0x11});
//        //CTR-R
//        keyMap.put(82, new byte[]{(byte) 0x12});
//        //CTR-S
//        keyMap.put(83, new byte[]{(byte) 0x13});
//        //CTR-T
//        keyMap.put(84, new byte[]{(byte) 0x14});
//        //CTR-U
//        keyMap.put(85, new byte[]{(byte) 0x15});
//        //CTR-V
//        keyMap.put(86, new byte[]{(byte) 0x16});
//        //CTR-W
//        keyMap.put(87, new byte[]{(byte) 0x17});
//        //CTR-X
//        keyMap.put(88, new byte[]{(byte) 0x18});
//        //CTR-Y
//        keyMap.put(89, new byte[]{(byte) 0x19});
//        //CTR-Z
//        keyMap.put(90, new byte[]{(byte) 0x1A});
        //CTR-[
        keyMap.put(219, new byte[]{(byte) 0x1B});
        //CTR-]
        keyMap.put(221, new byte[]{(byte) 0x1D});
        //INSERT
        keyMap.put(45, "\033[2~".getBytes());
        //PG UP
        keyMap.put(33, "\033[5~".getBytes());
        //PG DOWN
        keyMap.put(34, "\033[6~".getBytes());
        //END
        keyMap.put(35, "\033[4~".getBytes());
        //HOME
        keyMap.put(36, "\033[1~".getBytes());

    }
}
