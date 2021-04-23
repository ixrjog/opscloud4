package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_slb_acl_entry")
public class OcAliyunSlbAclEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 控制策略组ID
     */
    @Column(name = "slb_acl_id")
    private String slbAclId;

    /**
     * 访问控制条目ip
     */
    @Column(name = "slb_acl_entry_ip")
    private String slbAclEntryIp;

    /**
     * 访问控制条目备注
     */
    @Column(name = "slb_acl_entry_comment")
    private String slbAclEntryComment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取控制策略组ID
     *
     * @return slb_acl_id - 控制策略组ID
     */
    public String getSlbAclId() {
        return slbAclId;
    }

    /**
     * 设置控制策略组ID
     *
     * @param slbAclId 控制策略组ID
     */
    public void setSlbAclId(String slbAclId) {
        this.slbAclId = slbAclId;
    }

    /**
     * 获取访问控制条目ip
     *
     * @return slb_acl_entry_ip - 访问控制条目ip
     */
    public String getSlbAclEntryIp() {
        return slbAclEntryIp;
    }

    /**
     * 设置访问控制条目ip
     *
     * @param slbAclEntryIp 访问控制条目ip
     */
    public void setSlbAclEntryIp(String slbAclEntryIp) {
        this.slbAclEntryIp = slbAclEntryIp;
    }

    /**
     * 获取访问控制条目备注
     *
     * @return slb_acl_entry_comment - 访问控制条目备注
     */
    public String getSlbAclEntryComment() {
        return slbAclEntryComment;
    }

    /**
     * 设置访问控制条目备注
     *
     * @param slbAclEntryComment 访问控制条目备注
     */
    public void setSlbAclEntryComment(String slbAclEntryComment) {
        this.slbAclEntryComment = slbAclEntryComment;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}