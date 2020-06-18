package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;

@Table(name = "assets_systemuser_assets")
public class AssetsSystemuserAssets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "systemuser_id")
    private String systemuserId;

    @Column(name = "asset_id")
    private String assetId;

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

    /**
     * @return asset_id
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}