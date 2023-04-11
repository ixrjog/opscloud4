package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

public class Position {

    public static enum PositionType {

        TEXT, IMAGE;
        private static JacksonJsonEnumHelper<PositionType> enumHelper = new JacksonJsonEnumHelper<>(PositionType.class,
                false, false);

        @JsonCreator
        public static PositionType forValue(String value) {
            return enumHelper.forValue(value);
        }

        @JsonValue
        public String toValue() {
            return (enumHelper.toString(this));
        }

        @Override
        public String toString() {
            return (enumHelper.toString(this));
        }
    }

    private String baseSha;
    private String startSha;
    private String headSha;
    private String oldPath;
    private String newPath;
    private PositionType positionType;

    private Integer oldLine;
    private Integer newLine;

    private Integer width;
    private Integer height;
    private Double x;
    private Double y;

    public String getBaseSha() {
        return baseSha;
    }

    public void setBaseSha(String baseSha) {
        this.baseSha = baseSha;
    }

    public Position withBaseSha(String baseSha) {
        this.baseSha = baseSha;
        return (this);
    }

    public String getStartSha() {
        return startSha;
    }

    public void setStartSha(String startSha) {
        this.startSha = startSha;
    }

    public Position withStartSha(String startSha) {
        this.startSha = startSha;
        return (this);
    }

    public String getHeadSha() {
        return headSha;
    }

    public void setHeadSha(String headSha) {
        this.headSha = headSha;
    }

    public Position withHeadSha(String headSha) {
        this.headSha = headSha;
        return (this);
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public Position withOldPath(String oldPath) {
        this.oldPath = oldPath;
        return (this);
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public Position withNewPath(String newPath) {
        this.newPath = newPath;
        return (this);
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public Position withPositionType(PositionType positionType) {
        this.positionType = positionType;
        return (this);
    }

    public Integer getOldLine() {
        return oldLine;
    }

    public void setOldLine(Integer oldLine) {
        this.oldLine = oldLine;
    }

    public Position withOldLine(Integer oldLine) {
        this.oldLine = oldLine;
        return (this);
    }

    public Integer getNewLine() {
        return newLine;
    }

    public void setNewLine(Integer newLine) {
        this.newLine = newLine;
    }

    public Position withNewLine(Integer newLine) {
        this.newLine = newLine;
        return (this);
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Position withWidth(Integer width) {
        this.width = width;
        return (this);
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Position withHeight(Integer height) {
        this.height = height;
        return (this);
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Position withX(Double x) {
        this.x = x;
        return (this);
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Position withY(Double y) {
        this.y = y;
        return (this);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
