package com.sdg.cmdb.domain.ss;

import java.io.Serializable;

public class SsVO implements Serializable {

    private String server;

    private long port;

    private String qrcode;

    private String content;

    private boolean checked = false;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        String[] s = server.split(":");
        if (s.length == 0) {
            return;
        }
        if (s.length == 1) {
            this.server = server;
        }
        if (s.length == 2) {
            this.server = s[0];
            this.content = s[1];
        }

    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
