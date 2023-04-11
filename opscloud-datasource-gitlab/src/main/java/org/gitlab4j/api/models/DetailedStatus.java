package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

/**
 * This class is part of the Pipeline message.
 */
public class DetailedStatus {

    private String icon;
    private String text;
    private String label;
    private String group;
    private String tooltip;
    private Boolean hasDetails;
    private String detailsPath;
    private String illustration;
    private String favicon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public Boolean getHasDetails() {
        return hasDetails;
    }

    public void setHasDetails(Boolean hasDetails) {
        this.hasDetails = hasDetails;
    }

    public String getDetailsPath() {
        return detailsPath;
    }

    public void setDetailsPath(String detailsPath) {
        this.detailsPath = detailsPath;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
