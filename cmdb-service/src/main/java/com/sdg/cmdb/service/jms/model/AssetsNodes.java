package com.sdg.cmdb.service.jms.model;

public class AssetsNodes {
    private String id;

    private String key;

    private String value;

    private String parent;

    private String assets_amount;

    private String[] assets;

    private String[] nodes;

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    public String[] getAssets() {
        return assets;
    }

    public void setAssets(String[] assets) {
        this.assets = assets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAssets_amount() {
        return assets_amount;
    }

    public void setAssets_amount(String assets_amount) {
        this.assets_amount = assets_amount;
    }
}
