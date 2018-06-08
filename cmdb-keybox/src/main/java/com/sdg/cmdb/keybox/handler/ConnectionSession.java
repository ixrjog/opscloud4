package com.sdg.cmdb.keybox.handler;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxxiao on 2016/12/1.
 */
public class ConnectionSession {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionSession.class);

    /**
     * remote input stream
     */
    private InputStream inputStream;

    /**
     * remote output stream
     */
    private PrintStream printStream;

    /**
     * 链接实体
     */
    private Channel channel;

    /**
     * 链接会话
     */
    private Session session;

    private StringBuffer buffer = new StringBuffer();

    /**
     * 桥接集合
     */
    private Map<String, SessionBridge> bridgeMap = new ConcurrentHashMap<>();

    private boolean writeThreadFlag = true,readThreadFlag = true;

    public ConnectionSession(Channel channel, Session session) throws IOException {
        this.inputStream = channel.getInputStream();
        this.printStream = new PrintStream(channel.getOutputStream(), true);
        this.channel = channel;
        this.session = session;

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        new Thread(() -> {
            try (
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr)
            ) {
                char[] buff = new char[1024];
                int read;
                while (readThreadFlag) {
                    while (((read = br.read(buff)) != -1)) {
                        buffer.append(buff, 0, read);

                        Thread.sleep(50);
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.toString(), ex);
            }
        }).start();

        new Thread(() -> {
            try {
                while (writeThreadFlag) {
                    StringBuffer tmpBuffer = this.buffer;
                    this.buffer = new StringBuffer();
                    Thread.sleep(20);
                    if (tmpBuffer.length() == 0) {
                        continue;
                    }
                    for(SessionBridge bridge : bridgeMap.values()) {
                        bridge.process(tmpBuffer.toString());
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }).start();
    }

    /**
     * 终止任务
     */
    public void close() {
        this.channel.disconnect();
        this.session.disconnect();
        writeThreadFlag = false;
        readThreadFlag = false;
    }

    /**
     * 向远端写
     * @param cmd
     */
    public void writeToRemote(String cmd) {
        printStream.print(cmd);
    }

    /**
     * 向远端写
     * @param cmd
     */
    public void writeToRemote(byte[] cmd) throws IOException {
        printStream.write(cmd);
    }

    public void registerRead(String uniqueKey, SessionBridge sessionBridge) {
        bridgeMap.put(uniqueKey, sessionBridge);
    }

    public void unregisterRead(String uniqueKey) {
        bridgeMap.remove(uniqueKey);
    }

    public void setPtySize(int width, int height) {
        ((ChannelShell)channel).setPtySize((int) Math.floor(width / 7.2981),
                (int) Math.floor(height / 17), width, height);
    }
}
