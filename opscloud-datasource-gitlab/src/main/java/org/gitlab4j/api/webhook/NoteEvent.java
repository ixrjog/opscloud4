package org.gitlab4j.api.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.JacksonJson;
import org.gitlab4j.api.utils.JacksonJsonEnumHelper;

import java.util.Date;

public class NoteEvent extends AbstractEvent {

    public static final String X_GITLAB_EVENT = "Note Hook";
    public static final String OBJECT_KIND = "note";

    private User user;
    private Long projectId;
    private EventProject project;
    private EventRepository repository;
    private ObjectAttributes objectAttributes;
    private EventCommit commit;
    private EventIssue issue;
    private EventMergeRequest mergeRequest;
    private EventSnippet snippet;

    @Override
	public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public EventRepository getRepository() {
        return repository;
    }

    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }

    public ObjectAttributes getObjectAttributes() {
        return this.objectAttributes;
    }

    public void setObjectAttributes(ObjectAttributes objectAttributes) {
        this.objectAttributes = objectAttributes;
    }

    public EventCommit getCommit() {
        return commit;
    }

    public void setCommit(EventCommit commit) {
        this.commit = commit;
    }

    public EventIssue getIssue() {
        return issue;
    }

    public void setIssue(EventIssue issue) {
        this.issue = issue;
    }

    public EventMergeRequest getMergeRequest() {
        return mergeRequest;
    }

    public void setMergeRequest(EventMergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    public EventSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(EventSnippet snippet) {
        this.snippet = snippet;
    }

    public static enum NoteableType {

        ISSUE, MERGE_REQUEST, SNIPPET, COMMIT;
        private static JacksonJsonEnumHelper<NoteableType> enumHelper = new JacksonJsonEnumHelper<>(NoteableType.class, true, true);

        @JsonCreator
        public static NoteableType forValue(String value) {
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

    public static class ObjectAttributes {

        private Long id;
        private String note;
        private String discussionId;
        private String type;
        private NoteableType noteableType;
        private Long authorId;
        private Date createdAt;
        private Date updatedAt;
        private Long projectId;
        private String attachment;
        private String lineCode;
        private String commitId;
        private Long noteableId;
        private Boolean system;
        private Diff stDiff;
        private String url;

        public Long getId() {
            return this.id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDiscussionId() {
            return discussionId;
        }

        public void setDiscussionId(String discussionId) {
            this.discussionId = discussionId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public NoteableType getNoteableType() {
            return noteableType;
        }

        public void NoteableType(NoteableType notableType) {
            this.noteableType = notableType;
        }

        public Long getAuthorId() {
            return this.authorId;
        }

        public void setAuthorId(Long authorId) {
            this.authorId = authorId;
        }

        public Date getCreatedAt() {
            return this.createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return this.updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Long getProjectId() {
            return this.projectId;
        }

        public void setProjectId(Long projectId) {
            this.projectId = projectId;
        }

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public String getLineCode() {
            return lineCode;
        }

        public void setLineCode(String lineCode) {
            this.lineCode = lineCode;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public Long getNoteableId() {
            return noteableId;
        }

        public void setNoteableId(Long noteableId) {
            this.noteableId = noteableId;
        }

        public Boolean getSystem() {
            return system;
        }

        public void setSystem(Boolean system) {
            this.system = system;
        }

        public Diff getStDiff() {
            return stDiff;
        }

        public void setStDiff(Diff stDiff) {
            this.stDiff = stDiff;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
