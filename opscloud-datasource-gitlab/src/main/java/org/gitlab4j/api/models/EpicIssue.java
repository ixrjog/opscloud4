
package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Map;

public class EpicIssue extends Issue {

    private Integer downvotes;
    private Integer upvotes;

    @JsonProperty("_links")
    private Map<String, String> links;

    private Boolean subscribed;
    private Long epicIssueId;
    private Integer relativePosition;

    @Override
	public Integer getDownvotes() {
        return downvotes;
    }

    @Override
	public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    @Override
	public Integer getUpvotes() {
        return upvotes;
    }

    @Override
	public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    @JsonIgnore
    public String getLinkByName(String name) {
        if (links == null || links.isEmpty()) {
            return (null);
        }

        return (links.get(name));
    }

    @Override
	public Boolean getSubscribed() {
        return subscribed;
    }

    @Override
	public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Long getEpicIssueId() {
        return epicIssueId;
    }

    public void setEpicIssueId(Long epicIssueId) {
        this.epicIssueId = epicIssueId;
    }

    public Integer getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Integer relativePosition) {
        this.relativePosition = relativePosition;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
