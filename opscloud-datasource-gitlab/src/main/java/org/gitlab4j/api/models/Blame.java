package org.gitlab4j.api.models;

import org.gitlab4j.api.utils.JacksonJson;

import java.util.List;

public class Blame {

    private Commit commit;
    private List<String> lines;

    public Commit getCommit() {
	return commit;
    }

    public void setCommit(Commit commit) {
	this.commit = commit;
    }

    public List<String> getLines() {
	return lines;
    }

    public void setLines(List<String> lines) {
	this.lines = lines;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
