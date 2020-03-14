package com.baiyi.opscloud.domain.generator.jumpserver;

import javax.persistence.*;

@Table(name = "perms_assetpermission_nodes")
public class PermsAssetpermissionNodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "assetpermission_id")
    private String assetpermissionId;

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