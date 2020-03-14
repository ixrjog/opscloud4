package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;

@Table(name = "assets_asset_nodes")
public class AssetsAssetNodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "node_id")
    private String nodeId;

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

    /**
     * @return node_id
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}