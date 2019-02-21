package com.sdg.cmdb.domain.logService;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogFormatNginx implements Serializable {

    private static final long serialVersionUID = 1510529477713758294L;

    private long id;
    private long groupId;
    private String gmtLogtime;

    // TODO 后端服务器
    private ServerDO serverDO;
    /**
     * __source__:  192.168.8.80
     * __tag__:__hostname__:  nginx-7
     * __tag__:__path__:  /data/nginx/logs/member-benefits.myzebravip.com/access.log
     * __tag__:__receive_time__:  1548552728
     * __topic__:  group_nginx
     */
    // TODO __source__:  192.168.8.80 数据源服务器IP
    private String source;
    // TODO __tag__:__hostname__:  nginx-7
    private String hostname;
    // TODO __tag__:__path__:  /data/nginx/logs/member-benefits.myzebravip.com/access.log
    private String path;
    // TODO __tag__:__receive_time__:  1548552728
    private String receive_time;
    // TODO __topic__:  group_nginx
    private String topic;

    private String bytes_sent;
    private String host;
    private String http_MAC;
    private String http_channel;
    private String http_imei;
    private String http_imsi;
    private String http_latitude;
    private String http_longitude;
    private String http_os;
    private String http_phoneBrand;
    private String http_platform;
    private String http_realImei;
    private String http_referer;
    private String http_remark;
    private String http_requestId;
    private String http_session_id;
    private String http_timestamp;
    private String http_user_agent;
    private String http_version;
    private String http_x_forwarded_for;
    private String http_x_real_ip;
    private String remote_addr;
    private String request_length;
    private String request_method;
    private String request_time;
    private String request_uri;
    private String scheme;
    private String server_protocol;
    private String status;
    private String tcpinfo_rtt;
    private String time;
    private String upstream_addr;
    private String upstream_response_time;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
