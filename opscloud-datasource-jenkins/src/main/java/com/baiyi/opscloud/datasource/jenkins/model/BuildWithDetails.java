package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.helper.BuildConsoleStreamListener;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class BuildWithDetails extends Build {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String TEXT_SIZE_HEADER = "x-text-size";
    public static final String MORE_DATA_HEADER = "x-more-data";
    public static final BuildWithDetails BUILD_HAS_NEVER_RUN = new BuildWithDetails() {
        public List getActions() {
            return Collections.emptyList();
        }

        public List<Artifact> getArtifacts() {
            return Collections.emptyList();
        }

        public List<BuildCause> getCauses() {
            return Collections.emptyList();
        }

        public List<BuildChangeSetAuthor> getCulprits() {
            return Collections.emptyList();
        }

        public BuildResult getResult() {
            return BuildResult.NOT_BUILT;
        }
    };
    public static final BuildWithDetails BUILD_HAS_BEEN_CANCELLED = new BuildWithDetails() {
        public List getActions() {
            return Collections.emptyList();
        }

        public List<Artifact> getArtifacts() {
            return Collections.emptyList();
        }

        public List<BuildCause> getCauses() {
            return Collections.emptyList();
        }

        public List<BuildChangeSetAuthor> getCulprits() {
            return Collections.emptyList();
        }

        public BuildResult getResult() {
            return BuildResult.CANCELLED;
        }
    };
    private List actions;
    private boolean building;
    private String description;
    private String displayName;
    private long duration;
    private long estimatedDuration;
    private String fullDisplayName;
    private String id;
    private long timestamp;
    private BuildResult result;
    private List<Artifact> artifacts;
    private String consoleOutputText;
    private String consoleOutputHtml;
    private BuildChangeSet changeSet;
    @JsonProperty("changeSets")
    private List<BuildChangeSet> changeSets;
    private String builtOn;
    private List<BuildChangeSetAuthor> culprits;

    public BuildWithDetails() {
    }

    public BuildWithDetails(BuildWithDetails details) {
        this.actions = details.actions;
        this.description = details.description;
        this.displayName = details.displayName;
        this.building = details.building;
        this.duration = details.duration;
        this.estimatedDuration = details.estimatedDuration;
        this.fullDisplayName = details.fullDisplayName;
        this.id = details.id;
        this.timestamp = details.timestamp;
        this.result = details.result;
        this.artifacts = details.artifacts;
        this.consoleOutputHtml = details.consoleOutputHtml;
        this.consoleOutputText = details.consoleOutputText;
        this.changeSet = details.changeSet;
        this.builtOn = details.builtOn;
        this.culprits = details.culprits;
        this.setClient(details.getClient());
    }

    public List<Artifact> getArtifacts() {
        return this.artifacts;
    }

    public boolean isBuilding() {
        return this.building;
    }

    public List<BuildCause> getCauses() {
        Collection<?> causes = Collections2.filter(this.actions, new Predicate<Map<String, Object>>() {
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("causes");
            }
        });
        List<BuildCause> result = Lists.newArrayList();
        if (!causes.isEmpty()) {
            List<Map<String, Object>> causes_blob = (List) ((Map) causes.toArray()[0]).get("causes");
            Iterator<?> var4 = causes_blob.iterator();

            while (var4.hasNext()) {
                Map<String, Object> cause = (Map) var4.next();
                BuildCause convertToBuildCause = this.convertToBuildCause(cause);
                result.add(convertToBuildCause);
            }
        }

        return result;
    }

    public void updateDisplayNameAndDescription(String displayName, String description, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(displayName, "displayName is not allowed to be null.");
        Objects.requireNonNull(description, "description is not allowed to be null.");
        ImmutableMap<String, String> params = ImmutableMap.of("displayName", displayName, "description", description, "core:apply", "", "Submit", "Save");
        this.client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
    }

    public void updateDisplayNameAndDescription(String displayName, String description) throws IOException {
        this.updateDisplayNameAndDescription(displayName, description, false);
    }

    public void updateDisplayName(String displayName, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(displayName, "displayName is not allowed to be null.");
        String description = this.getDescription() == null ? "" : this.getDescription();
        ImmutableMap<String, String> params = ImmutableMap.of("displayName", displayName, "description", description, "core:apply", "", "Submit", "Save");
        this.client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
    }

    public void updateDisplayName(String displayName) throws IOException {
        this.updateDisplayName(displayName, false);
    }

    public void updateDescription(String description, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(description, "description is not allowed to be null.");
        String displayName = this.getDisplayName() == null ? "" : this.getDisplayName();
        ImmutableMap<String, String> params = ImmutableMap.of("displayName", displayName, "description", description, "core:apply", "", "Submit", "Save");
        this.client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
    }

    public void updateDescription(String description) throws IOException {
        this.updateDescription(description, false);
    }

    private BuildCause convertToBuildCause(Map<String, Object> cause) {
        BuildCause cause_object = new BuildCause();
        String description = (String) cause.get("shortDescription");
        if (!Strings.isNullOrEmpty(description)) {
            cause_object.setShortDescription(description);
        }

        Integer upstreamBuild = (Integer) cause.get("upstreamBuild");
        if (upstreamBuild != null) {
            cause_object.setUpstreamBuild(upstreamBuild);
        }

        String upstreamProject = (String) cause.get("upstreamProject");
        if (!Strings.isNullOrEmpty(upstreamProject)) {
            cause_object.setUpstreamProject(upstreamProject);
        }

        String upstreamUrl = (String) cause.get("upstreamUrl");
        if (!Strings.isNullOrEmpty(upstreamUrl)) {
            cause_object.setUpstreamUrl(upstreamUrl);
        }

        String userId = (String) cause.get("userId");
        if (!Strings.isNullOrEmpty(userId)) {
            cause_object.setUserId(userId);
        }

        String userName = (String) cause.get("userName");
        if (!Strings.isNullOrEmpty(userName)) {
            cause_object.setUserName(userName);
        }

        return cause_object;
    }

    public String getDescription() {
        return this.description;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getEstimatedDuration() {
        return this.estimatedDuration;
    }

    public String getFullDisplayName() {
        return this.fullDisplayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getId() {
        return this.id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public BuildResult getResult() {
        return this.result;
    }

    public String getBuiltOn() {
        return this.builtOn;
    }

    public List getActions() {
        return this.actions;
    }

    public Map<String, String> getParameters() {
        Collection parameters = Collections2.filter(this.actions, new Predicate<Map<String, Object>>() {
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("parameters");
            }
        });
        Map<String, String> params = new HashMap();
        if (parameters != null && !parameters.isEmpty()) {
            Iterator var3 = ((List) ((Map) parameters.toArray()[0]).get("parameters")).iterator();

            while (var3.hasNext()) {
                Map<String, Object> param = (Map) var3.next();
                String key = (String) param.get("name");
                Object value = param.get("value");
                params.put(key, String.valueOf(value));
            }
        }

        return params;
    }

    public String getConsoleOutputText() throws IOException {
        return this.client.get(this.getUrl() + "/logText/progressiveText");
    }

    public String getConsoleOutputHtml() throws IOException {
        return this.client.get(this.getUrl() + "/logText/progressiveHtml");
    }

    public void streamConsoleOutput(BuildConsoleStreamListener listener, long poolingInterval, long poolingTimeout) throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();
        long timeoutTime = startTime + poolingTimeout * 1000;
        int bufferOffset = 0;

        while (true) {
            Thread.sleep(poolingInterval * 1000);
            ConsoleLog consoleLog = null;
            consoleLog = this.getConsoleOutputText(bufferOffset);
            String logString = consoleLog.getConsoleLog();
            if (logString != null && !logString.isEmpty()) {
                listener.onData(logString);
            }

            if (consoleLog.getHasMoreData()) {
                bufferOffset = consoleLog.getCurrentBufferSize();
                long var11 = System.currentTimeMillis();
                if (var11 <= timeoutTime) {
                    continue;
                }
                log.warn("Pooling for build {} for {} timeout! Check if job stuck in jenkins", this.getDisplayName(), this.getNumber());
                break;
            }

            listener.finished();
            break;
        }

    }

    public ConsoleLog getConsoleOutputText(int bufferOffset) throws IOException {
        List<NameValuePair> formData = new ArrayList();
        formData.add(new BasicNameValuePair("start", Integer.toString(bufferOffset)));
        String path = this.getUrl() + "logText/progressiveText";
        HttpResponse httpResponse = this.client.post_form_with_result(path, formData, false);
        Header moreDataHeader = httpResponse.getFirstHeader("x-more-data");
        Header textSizeHeader = httpResponse.getFirstHeader("x-text-size");
        String response = EntityUtils.toString(httpResponse.getEntity());
        boolean hasMoreData = false;
        if (moreDataHeader != null) {
            hasMoreData = Boolean.TRUE.toString().equals(moreDataHeader.getValue());
        }

        int currentBufferSize = bufferOffset;
        if (textSizeHeader != null) {
            try {
                currentBufferSize = Integer.parseInt(textSizeHeader.getValue());
            } catch (NumberFormatException var11) {
                log.warn("Cannot parse buffer size for job {} build {}. Using current offset!", this.getDisplayName(), this.getNumber());
            }
        }

        return new ConsoleLog(response, hasMoreData, currentBufferSize);
    }

    public BuildChangeSet getChangeSet() {
        BuildChangeSet result;
        if (this.changeSet != null) {
            result = this.changeSet;
        } else if (this.changeSets != null && !this.changeSets.isEmpty()) {
            result = (BuildChangeSet) this.changeSets.getFirst();
        } else {
            result = null;
        }

        return result;
    }

    public void setChangeSet(BuildChangeSet changeSet) {
        this.changeSet = changeSet;
    }

    public List<BuildChangeSet> getChangeSets() {
        List result;
        if (this.changeSets != null) {
            result = this.changeSets;
        } else if (this.changeSet != null) {
            result = Collections.singletonList(this.changeSet);
        } else {
            result = null;
        }

        return result;
    }

    public void setChangeSets(List<BuildChangeSet> changeSets) {
        this.changeSets = changeSets;
    }

    public List<BuildChangeSetAuthor> getCulprits() {
        return this.culprits;
    }

    public void setCulprits(List<BuildChangeSetAuthor> culprits) {
        this.culprits = culprits;
    }

    public void setResult(BuildResult result) {
        this.result = result;
    }

    public InputStream downloadArtifact(Artifact a) throws IOException, URISyntaxException {
        URI uri = new URI(this.getUrl());
        String artifactPath = uri.getPath() + "artifact/" + a.getRelativePath();
        URI artifactUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), artifactPath, "", "");
        return this.client.getFile(artifactUri);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            BuildWithDetails other = (BuildWithDetails) obj;
            if (this.actions == null) {
                if (other.actions != null) {
                    return false;
                }
            } else if (!this.actions.equals(other.actions)) {
                return false;
            }

            if (this.artifacts == null) {
                if (other.artifacts != null) {
                    return false;
                }
            } else if (!this.artifacts.equals(other.artifacts)) {
                return false;
            }

            if (this.building != other.building) {
                return false;
            } else {
                if (this.builtOn == null) {
                    if (other.builtOn != null) {
                        return false;
                    }
                } else if (!this.builtOn.equals(other.builtOn)) {
                    return false;
                }

                if (this.changeSet == null) {
                    if (other.changeSet != null) {
                        return false;
                    }
                } else if (!this.changeSet.equals(other.changeSet)) {
                    return false;
                }

                if (this.changeSets == null) {
                    if (other.changeSets != null) {
                        return false;
                    }
                } else if (!this.changeSets.equals(other.changeSets)) {
                    return false;
                }

                if (this.consoleOutputHtml == null) {
                    if (other.consoleOutputHtml != null) {
                        return false;
                    }
                } else if (!this.consoleOutputHtml.equals(other.consoleOutputHtml)) {
                    return false;
                }

                if (this.consoleOutputText == null) {
                    if (other.consoleOutputText != null) {
                        return false;
                    }
                } else if (!this.consoleOutputText.equals(other.consoleOutputText)) {
                    return false;
                }

                if (this.culprits == null) {
                    if (other.culprits != null) {
                        return false;
                    }
                } else if (!this.culprits.equals(other.culprits)) {
                    return false;
                }

                if (this.description == null) {
                    if (other.description != null) {
                        return false;
                    }
                } else if (!this.description.equals(other.description)) {
                    return false;
                }

                if (this.displayName == null) {
                    if (other.displayName != null) {
                        return false;
                    }
                } else if (!this.displayName.equals(other.displayName)) {
                    return false;
                }

                if (this.duration != other.duration) {
                    return false;
                } else if (this.estimatedDuration != other.estimatedDuration) {
                    return false;
                } else {
                    if (this.fullDisplayName == null) {
                        if (other.fullDisplayName != null) {
                            return false;
                        }
                    } else if (!this.fullDisplayName.equals(other.fullDisplayName)) {
                        return false;
                    }

                    if (this.id == null) {
                        if (other.id != null) {
                            return false;
                        }
                    } else if (!this.id.equals(other.id)) {
                        return false;
                    }

                    if (this.result != other.result) {
                        return false;
                    } else {
                        return this.timestamp == other.timestamp;
                    }
                }
            }
        }
    }

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actions == null) ? 0 : actions.hashCode());
        result = prime * result + ((artifacts == null) ? 0 : artifacts.hashCode());
        result = prime * result + (building ? 1231 : 1237);
        result = prime * result + ((builtOn == null) ? 0 : builtOn.hashCode());
        result = prime * result + ((changeSet == null) ? 0 : changeSet.hashCode());
        result = prime * result + ((changeSets == null) ? 0 : changeSets.hashCode());
        result = prime * result + ((consoleOutputHtml == null) ? 0 : consoleOutputHtml.hashCode());
        result = prime * result + ((consoleOutputText == null) ? 0 : consoleOutputText.hashCode());
        result = prime * result + ((culprits == null) ? 0 : culprits.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + (int) (estimatedDuration ^ (estimatedDuration >>> 32));
        result = prime * result + ((fullDisplayName == null) ? 0 : fullDisplayName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

}