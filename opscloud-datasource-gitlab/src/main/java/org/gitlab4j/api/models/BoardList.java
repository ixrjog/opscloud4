package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

public class BoardList {

    private Long id;
    private Label label;
    private Integer position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
