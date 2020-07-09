package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;

@Table(name = "ops_adhoc_hosts")
public class OpsAdhocHosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "adhoc_id")
    private String adhocId;

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
     * @return adhoc_id
     */
    public String getAdhocId() {
        return adhocId;
    }

    /**
     * @param adhocId
     */
    public void setAdhocId(String adhocId) {
        this.adhocId = adhocId;
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