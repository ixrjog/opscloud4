package org.gitlab4j.api.webhook;

import org.gitlab4j.api.models.User;
import org.gitlab4j.api.utils.JacksonJson;

public class WikiPageEvent extends AbstractEvent {

    public static final String X_GITLAB_EVENT = "Wiki Page Hook";
    public static final String OBJECT_KIND = "wiki_page";

    private User user;
    private EventProject project;
    private Wiki wiki;
    private ObjectAttributes objectAttributes;

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

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public Wiki getWiki() {
        return wiki;
    }

    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
    }

    public ObjectAttributes getObjectAttributes() {
        return this.objectAttributes;
    }

    public void setObjectAttributes(ObjectAttributes objectAttributes) {
        this.objectAttributes = objectAttributes;
    }

    public static class Wiki {

        private String webUrl;
        private String git_http_url;
        private String git_ssh_url;
        private String pathWithNamespace;
        private String defaultBranch;

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getGit_http_url() {
            return git_http_url;
        }

        public void setGit_http_url(String git_http_url) {
            this.git_http_url = git_http_url;
        }

        public String getGit_ssh_url() {
            return git_ssh_url;
        }

        public void setGit_ssh_url(String git_ssh_url) {
            this.git_ssh_url = git_ssh_url;
        }

        public String getPathWithNamespace() {
            return pathWithNamespace;
        }

        public void setPathWithNamespace(String pathWithNamespace) {
            this.pathWithNamespace = pathWithNamespace;
        }

        public String getDefaultBranch() {
            return defaultBranch;
        }

        public void setDefaultBranch(String defaultBranch) {
            this.defaultBranch = defaultBranch;
        }
    }

    public static class ObjectAttributes {

        private String title;
        private String content;
        private String format;
        private String message;
        private String slug;
        private String url;
        private String action;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
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
