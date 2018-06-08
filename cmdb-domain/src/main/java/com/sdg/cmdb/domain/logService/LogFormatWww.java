package com.sdg.cmdb.domain.logService;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class LogFormatWww implements Serializable {

    private static final long serialVersionUID = 1510529477713758294L;

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

    // 拆分 "POST /itemcenter/pos/getItemIdsByTime HTTP/1.0"
    private String request;
    private String method;
    private String uri;
    private String protocol;

    private String args;
    private String[] argsList;


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

    private String redcatUserMobile;
    private String redcatUserLandmarkId;
    private String logisticsMobile;
    private String redcatIserSellerMobile;
    private String cm;
    private String uk;
    private String traceId;
    private String redcatUserSession;
    private String redcatUserSellerSession;
    private String logisticsSession;
    private String mac;
    private String sdgClient;

    private ServerDO serverDO;


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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;

        String[] s = request.split(" ");
        try {
            this.method = s[0];
            this.uri = s[1];
            this.protocol = s[2];
        } catch (Exception e) {

        }
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

    public String getRedcatUserMobile() {
        return redcatUserMobile;
    }

    public void setRedcatUserMobile(String redcatUserMobile) {
        this.redcatUserMobile = redcatUserMobile;
    }

    public String getRedcatUserLandmarkId() {
        return redcatUserLandmarkId;
    }

    public void setRedcatUserLandmarkId(String redcatUserLandmarkId) {
        this.redcatUserLandmarkId = redcatUserLandmarkId;
    }

    public String getLogisticsMobile() {
        return logisticsMobile;
    }

    public void setLogisticsMobile(String logisticsMobile) {
        this.logisticsMobile = logisticsMobile;
    }

    public String getRedcatIserSellerMobile() {
        return redcatIserSellerMobile;
    }

    public void setRedcatIserSellerMobile(String redcatIserSellerMobile) {
        this.redcatIserSellerMobile = redcatIserSellerMobile;
    }

    public String getCm() {
        return cm;
    }

    public void setCm(String cm) {
        this.cm = cm;
    }

    public String getUk() {
        return uk;
    }

    public void setUk(String uk) {
        this.uk = uk;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRedcatUserSession() {
        return redcatUserSession;
    }

    public void setRedcatUserSession(String redcatUserSession) {
        this.redcatUserSession = redcatUserSession;
    }

    public String getRedcatUserSellerSession() {
        return redcatUserSellerSession;
    }

    public void setRedcatUserSellerSession(String redcatUserSellerSession) {
        this.redcatUserSellerSession = redcatUserSellerSession;
    }

    public String getLogisticsSession() {
        return logisticsSession;
    }

    public void setLogisticsSession(String logisticsSession) {
        this.logisticsSession = logisticsSession;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSdgClient() {
        return sdgClient;
    }

    public void setSdgClient(String sdgClient) {
        this.sdgClient = sdgClient;
    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    @Override
    public String toString() {
        return "LogFormatWww{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", gmtLogtime='" + gmtLogtime + '\'' +
                ", hostname='" + hostname + '\'' +
                ", slbAddr='" + slbAddr + '\'' +
                ", source='" + source + '\'' +
                ", remoteUser='" + remoteUser + '\'' +
                ", timeLocal='" + timeLocal + '\'' +
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
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
