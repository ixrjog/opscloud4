package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.models.User;
import org.gitlab4j.api.models.Variable;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Date;
import java.util.List;

public class PipelineEvent extends AbstractEvent {

    public static final String X_GITLAB_EVENT = "Pipeline Hook";
    public static final String OBJECT_KIND = "pipeline";

    private ObjectAttributes objectAttributes;
    private User user;
    private EventProject project;
    private EventCommit commit;
    private List<Job> jobs;

    public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public ObjectAttributes getObjectAttributes() {
        return this.objectAttributes;
    }

    public void setObjectAttributes(ObjectAttributes objectAttributes) {
        this.objectAttributes = objectAttributes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public EventCommit getCommit() {
        return commit;
    }

    public void setCommit(EventCommit commit) {
        this.commit = commit;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public static class ObjectAttributes {

        private Long id;
        private String ref;
        private Boolean tag;
        private String sha;
        private String beforeSha;
        private String source;
        private String status;
        private List<String> stages;
        private Date createdAt;
        private Date finishedAt;
        private Integer duration;
        private List<Variable> variables;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public Boolean getTag() {
            return tag;
        }

        public void setTag(Boolean tag) {
            this.tag = tag;
        }

        public String getSha() {
            return sha;
        }

        public void setSha(String sha) {
            this.sha = sha;
        }

        public String getBeforeSha() {
            return beforeSha;
        }

        public void setBeforeSha(String beforeSha) {
            this.beforeSha = beforeSha;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<String> getStages() {
            return stages;
        }

        public void setStages(List<String> stages) {
            this.stages = stages;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getFinishedAt() {
            return finishedAt;
        }

        public void setFinishedAt(Date finishedAt) {
            this.finishedAt = finishedAt;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public List<Variable> getVariables() {
            return variables;
        }

        public void setVariables(List<Variable> variables) {
            this.variables = variables;
        }
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
