package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;
import java.util.Date;

@Table(name = "assets_asset")
public class AssetsAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String ip;

    private String hostname;

    private Integer port;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "public_ip")
    private String publicIp;

    private String number;

    private String vendor;

    private String model;

    private String sn;

    @Column(name = "cpu_model")
    private String cpuModel;

    @Column(name = "cpu_count")
    private Integer cpuCount;

    @Column(name = "cpu_cores")
    private Integer cpuCores;

    private String memory;

    @Column(name = "disk_total")
    private String diskTotal;

    @Column(name = "disk_info")
    private String diskInfo;

    private String os;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "os_arch")
    private String osArch;

    @Column(name = "hostname_raw")
    private String hostnameRaw;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "admin_user_id")
    private String adminUserId;

    @Column(name = "domain_id")
    private String domainId;

    private String protocol;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "cpu_vcpus")
    private Integer cpuVcpus;

    private String protocols;

    @Column(name = "platform_id")
    private Integer platformId;

    private String comment;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @return is_active
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return public_ip
     */
    public String getPublicIp() {
        return publicIp;
    }

    /**
     * @param publicIp
     */
    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    /**
     * @return number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * @return cpu_model
     */
    public String getCpuModel() {
        return cpuModel;
    }

    /**
     * @param cpuModel
     */
    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    /**
     * @return cpu_count
     */
    public Integer getCpuCount() {
        return cpuCount;
    }

    /**
     * @param cpuCount
     */
    public void setCpuCount(Integer cpuCount) {
        this.cpuCount = cpuCount;
    }

    /**
     * @return cpu_cores
     */
    public Integer getCpuCores() {
        return cpuCores;
    }

    /**
     * @param cpuCores
     */
    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    /**
     * @return memory
     */
    public String getMemory() {
        return memory;
    }

    /**
     * @param memory
     */
    public void setMemory(String memory) {
        this.memory = memory;
    }

    /**
     * @return disk_total
     */
    public String getDiskTotal() {
        return diskTotal;
    }

    /**
     * @param diskTotal
     */
    public void setDiskTotal(String diskTotal) {
        this.diskTotal = diskTotal;
    }

    /**
     * @return disk_info
     */
    public String getDiskInfo() {
        return diskInfo;
    }

    /**
     * @param diskInfo
     */
    public void setDiskInfo(String diskInfo) {
        this.diskInfo = diskInfo;
    }

    /**
     * @return os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return os_version
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @param osVersion
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return os_arch
     */
    public String getOsArch() {
        return osArch;
    }

    /**
     * @param osArch
     */
    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    /**
     * @return hostname_raw
     */
    public String getHostnameRaw() {
        return hostnameRaw;
    }

    /**
     * @param hostnameRaw
     */
    public void setHostnameRaw(String hostnameRaw) {
        this.hostnameRaw = hostnameRaw;
    }

    /**
     * @return created_by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return date_created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return admin_user_id
     */
    public String getAdminUserId() {
        return adminUserId;
    }

    /**
     * @param adminUserId
     */
    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    /**
     * @return domain_id
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * @param domainId
     */
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    /**
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return org_id
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return cpu_vcpus
     */
    public Integer getCpuVcpus() {
        return cpuVcpus;
    }

    /**
     * @param cpuVcpus
     */
    public void setCpuVcpus(Integer cpuVcpus) {
        this.cpuVcpus = cpuVcpus;
    }

    /**
     * @return protocols
     */
    public String getProtocols() {
        return protocols;
    }

    /**
     * @param protocols
     */
    public void setProtocols(String protocols) {
        this.protocols = protocols;
    }

    /**
     * @return platform_id
     */
    public Integer getPlatformId() {
        return platformId;
    }

    /**
     * @param platformId
     */
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}