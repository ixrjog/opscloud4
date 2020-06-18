package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;

@Table(name = "perms_assetpermission_system_users")
public class PermsAssetpermissionSystemUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "assetpermission_id")
    private String assetpermissionId;

    @Column(name = "systemuser_id")
    private String systemuserId;

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
     * @return systemuser_id
     */
    public String getSystemuserId() {
        return systemuserId;
    }

    /**
     * @param systemuserId
     */
    public void setSystemuserId(String systemuserId) {
        this.systemuserId = systemuserId;
    }
}