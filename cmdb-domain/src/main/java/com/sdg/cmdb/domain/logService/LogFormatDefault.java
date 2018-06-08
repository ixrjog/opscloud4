package com.sdg.cmdb.domain.logService;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class LogFormatDefault implements Serializable {


    private static final long serialVersionUID = 3379055034898151544L;
    private long id;
    private long groupId;
    // 日志采集时间
    private String gmtLogtime;
    // 日志源服务器名称
    private String hostname;
    // 日志源服务器IP
    private String source;

    private String topic;
    // 日志路径
    private String path;
    // 日志内容
    private String content;

    private ServerDO serverDO;


    @Override
    public String toString() {
        return "LogFormatDefault{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", gmtLogtime='" + gmtLogtime + '\'' +
                ", hostname='" + hostname + '\'' +
                ", source='" + source + '\'' +
                ", topic='" + topic + '\'' +
                ", path='" + path + '\'' +
                ", content='" + content + '\'' +

                ", serverDO=" + serverDO +
                '}';
    }


    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGmtLogtime() {
        return gmtLogtime;
    }

    public void setGmtLogtime(String gmtLogtime) {
        this.gmtLogtime = gmtLogtime;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
