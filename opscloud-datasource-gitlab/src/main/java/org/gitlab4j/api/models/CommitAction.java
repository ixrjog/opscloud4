package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.Constants.Encoding;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.utils.FileUtils;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

import java.io.File;
import java.io.IOException;

public class CommitAction {

    public enum Action {

        CREATE, DELETE, MOVE, UPDATE, CHMOD;

        private static JacksonJsonEnumHelper<Action> enumHelper = new JacksonJsonEnumHelper<>(Action.class);

        @JsonCreator
        public static Action forValue(String value) {
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

    private Action action;
    private String filePath;
    private String previousPath;
    private String content;
    private Encoding encoding;
    private String lastCommitId;
    private Boolean executeFilemode;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public CommitAction withAction(Action action) {
        this.action = action;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public CommitAction withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getPreviousPath() {
        return previousPath;
    }

    public void setPreviousPath(String previousPath) {
        this.previousPath = previousPath;
    }

    public CommitAction withPreviousPath(String previousPath) {
        this.previousPath = previousPath;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommitAction withContent(String content) {
        this.content = content;
        return this;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public CommitAction withEncoding(Encoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public String getLastCommitId() {
        return lastCommitId;
    }

    public void setLastCommitId(String lastCommitId) {
        this.lastCommitId = lastCommitId;
    }

    public CommitAction withLastCommitId(String lastCommitId) {
        this.lastCommitId = lastCommitId;
        return this;
    }

    public Boolean getExecuteFilemode() {
        return executeFilemode;
    }

    public void setExecuteFilemode(Boolean executeFilemode) {
        this.executeFilemode = executeFilemode;
    }

    public CommitAction withExecuteFilemode(Boolean executeFilemode) {
        this.executeFilemode = executeFilemode;
        return this;
    }

    public CommitAction withFileContent(String filePath, Encoding encoding) throws GitLabApiException {
        File file = new File(filePath);
        return (withFileContent(file, filePath, encoding));
    }

    public CommitAction withFileContent(File file, String filePath, Encoding encoding) throws GitLabApiException {

        this.encoding = (encoding != null ? encoding : Encoding.TEXT); 
        this.filePath = filePath;

        try {
            content = FileUtils.getFileContentAsString(file, this.encoding);
        } catch (IOException e) {
            throw new GitLabApiException(e);
        }

        return (this);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
