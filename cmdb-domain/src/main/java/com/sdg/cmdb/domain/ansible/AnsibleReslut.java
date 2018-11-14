package com.sdg.cmdb.domain.ansible;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public class AnsibleReslut implements Serializable {

    private static final long serialVersionUID = 9152678573722180439L;

    public AnsibleReslut() {

    }

    public AnsibleReslut(String header, String body) {
        this.header = header;
        this.body = body;
        if (!StringUtils.isEmpty(this.header)) {
            String[] h = this.header.split("\\|");
            this.host = h[0];
            this.reslut = h[1];
            if (this.reslut.equalsIgnoreCase("SUCCESS"))
                this.success = true;
        }
    }

    private String header;

    private String body;

    private String host;

    private String reslut;

    private boolean success;


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getReslut() {
        return reslut;
    }

    public void setReslut(String reslut) {
        this.reslut = reslut;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
