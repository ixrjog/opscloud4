package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class TreeItem {

    public enum Type {
        TREE, BLOB, COMMIT;

        public String toString() {
            return (name().toLowerCase());
        }
    }

    private String id;
    private String mode;
    private String name;
    private String path;
    private Type type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
