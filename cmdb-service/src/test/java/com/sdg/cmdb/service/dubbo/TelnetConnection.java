package com.sdg.cmdb.service.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

@Slf4j
public class TelnetConnection {


    private TelnetClient telnet = null;

    //	private String username = "";
    private String password = "";
    private String prompt = ">";

    private InputStream in;
    private PrintStream out;

    public TelnetConnection(String host, int port) {
        if (telnet == null) {
            telnet = new TelnetClient();
            try {
                telnet.connect(host, port);
                in = telnet.getInputStream();
                out = new PrintStream(telnet.getOutputStream());
            } catch (SocketException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param prompt the prompt to set
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * 关闭打开的连接
     *
     * @param telnet
     */
    public void close(TelnetClient telnet) {
        if (telnet != null) {
            try {
                telnet.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (this.telnet != null) {
            try {
                this.telnet.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 登录到远程机器中
     */
    public void login(String username, String password, String prompt) {
        //处理命令行的提示字符
        if (prompt == null || "".equals(prompt)) {
            if (this.prompt.equals("#"))
                prompt = ("root".equals(username)) ? "#" : "$";
        } else {
            this.prompt = prompt;
        }
        this.password = password;
        readUntil("Username:");
        write(username);
        readUntil("Password:");
        write(this.password);
        readUntil(this.prompt + "");
        // 其它交换机登录后如果有提示信息也需要添加
        if (this.prompt.indexOf("Quidway") != -1)
            readUntil("login");
    }

    /**
     * 读取分析结果 * * @param pattern * @return
     */
    public String readUntil(String pattern) {
        StringBuffer sb = new StringBuffer();

        try {
            int len = 0;
            while ((len = in.read()) != -1) {
                sb.append((char) len);
                if (sb.toString().endsWith(pattern)) {
                    return sb.toString();
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return "";
    }


    /**
     * 写操作 * * @param value
     */
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向目标发送命令字符串 * * @param command * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(prompt + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
