package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;

@Table(name = "perms_assetpermission_user_groups")
public class PermsAssetpermissionUserGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "assetpermission_id")
    private String assetpermissionId;

    @Column(name = "usergroup_id")
    private String usergroupId;

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
     * @return assetpermission_id
     */
    public String getAssetpermissionId() {
        return assetpermissionId;
    }

    /**
     * @param assetpermissionId
     */
    public void setAssetpermissionId(String assetpermissionId) {
        this.assetpermissionId = assetpermissionId;
    }

    /**
     * @return usergroup_id
     */
    public String getUsergroupId() {
        return usergroupId;
    }

    /**
     * @param usergroupId
     */
    public void setUsergroupId(String usergroupId) {
        this.usergroupId = usergroupId;
    }
}