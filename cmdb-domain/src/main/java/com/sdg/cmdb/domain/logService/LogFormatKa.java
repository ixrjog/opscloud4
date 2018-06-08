package com.sdg.cmdb.domain.logService;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class LogFormatKa implements Serializable {

    private static final long serialVersionUID = -7338045077446138382L;

    private long id;
    private long groupId;
    private String gmtLogtime;
    private String hostname;

    private String packId;
    private String path;
    private String topic;
    // 数据源服务器IP
    private String source;

    private String slbAddr;
    private String remoteUser;
    private String timeLocal;
    private String method;
    private String uri;
    private String args;
    private String[] argsList;

    private String protocol;
    private String status;
    private String bodyBytesSent;
    private String referer;
    private String userAgent;
    private String sourceIp;
    private String requestTime;
    private String upstreamResponseTime;
    private String upstreamAddr;
    private String pipe;
    private String token;
    private String udid;
    private String lat;
    private String lng;
    private String mobile;
    private String traceId;
    private String nss;

    private ServerDO serverDO;

    @Override
    public String toString() {
        return "LogFormatKa{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", gmtLogtime='" + gmtLogtime + '\'' +
                ", hostname='" + hostname + '\'' +
                ", slbAddr='" + slbAddr + '\'' +
                ", source='" + source + '\'' +
                ", remoteUser='" + remoteUser + '\'' +
                ", timeLocal='" + timeLocal + '\'' +
                ", uri='" + uri + '\'' +
                ", args='" + args + '\'' +
                ", protocol='" + protocol + '\'' +
                ", status='" + status + '\'' +
                ", bodyBytesSent='" + bodyBytesSent + '\'' +
                ", referer='" + referer + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", sourceIp='" + sourceIp + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", upstreamResponseTime='" + upstreamResponseTime + '\'' +
                ", upstreamAddr='" + upstreamAddr + '\'' +
                ", token='" + token + '\'' +
                ", udid='" + udid + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", mobile='" + mobile + '\'' +
                ", traceId='" + traceId + '\'' +
                ", nss='" + nss + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSlbAddr() {
        return slbAddr;
    }

    public void setSlbAddr(String slbAddr) {
        this.slbAddr = slbAddr;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public String getTimeLocal() {
        return timeLocal;
    }

    public void setTimeLocal(String timeLocal) {
        this.timeLocal = timeLocal;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
        this.argsList = args.split("&");
    }

    public String[] getArgsList() {
        return argsList;
    }

    public void setArgsList(String[] argsList) {
        this.argsList = argsList;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBodyBytesSent() {
        return bodyBytesSent;
    }

    public void setBodyBytesSent(String bodyBytesSent) {
        this.bodyBytesSent = bodyBytesSent;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getUpstreamResponseTime() {
        return upstreamResponseTime;
    }

    public void setUpstreamResponseTime(String upstreamResponseTime) {
        this.upstreamResponseTime = upstreamResponseTime;
    }

    public String getUpstreamAddr() {
        return upstreamAddr;
    }

    public void setUpstreamAddr(String upstreamAddr) {
        this.upstreamAddr = upstreamAddr;
    }

    public String getPipe() {
        return pipe;
    }

    public void setPipe(String pipe) {
        this.pipe = pipe;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }
}
