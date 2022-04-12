/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.baiyi.opscloud.datasource.jenkins.helper.BuildConsoleStreamListener;
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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * This class represents build information with details about what has been done
 * like duration start and of course the build result.
 *
 */
public class BuildWithDetails extends Build {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public final static String TEXT_SIZE_HEADER = "x-text-size";
    public final static String MORE_DATA_HEADER = "x-more-data";

    /**
     * This will be returned by the API in cases where the build has never run.
     * For example {@link Build#BUILD_HAS_NEVER_RUN}
     */
    public static final BuildWithDetails BUILD_HAS_NEVER_RUN = new BuildWithDetails() {

        @Override
        public List getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<Artifact> getArtifacts() {
            return Collections.<Artifact>emptyList();
        }

        @Override
        public List<BuildCause> getCauses() {
            return Collections.<BuildCause>emptyList();
        }

        @Override
        public List<BuildChangeSetAuthor> getCulprits() {
            return Collections.<BuildChangeSetAuthor>emptyList();
        }

        @Override
        public BuildResult getResult() {
            return BuildResult.NOT_BUILT;
        }

    };

    /**
     * This will be returned by the API in cases where the build has been
     * cancelled. For example {@link Build#BUILD_HAS_BEEN_CANCELLED}
     */
    public static final BuildWithDetails BUILD_HAS_BEEN_CANCELLED = new BuildWithDetails() {

        @Override
        public List getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<Artifact> getArtifacts() {
            return Collections.<Artifact>emptyList();
        }

        @Override
        public List<BuildCause> getCauses() {
            return Collections.<BuildCause>emptyList();
        }

        @Override
        public List<BuildChangeSetAuthor> getCulprits() {
            return Collections.<BuildChangeSetAuthor>emptyList();
        }

        @Override
        public BuildResult getResult() {
            return BuildResult.CANCELLED;
        }

    };

    private List<LinkedHashMap<String, List<LinkedHashMap<String, Object>>>> actions; // TODO: Should be improved.
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
        // Default ctor is needed to jackson.
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
        return artifacts;
    }

    public boolean isBuilding() {
        return building;
    }

    public List<BuildCause> getCauses() {
        return actions.stream()
                .filter(item -> item.containsKey("causes"))
                .flatMap(item -> item.entrySet().stream())
                .flatMap(sub -> sub.getValue().stream())
                .map(item -> convertToBuildCause(item))
                .collect(toList());
    }

    /**
     * Update <code>displayName</code> and the <code>description</code> of a
     * build.
     * 
     * @param displayName The new displayName which should be set.
     * @param description The description which should be set.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public BuildWithDetails updateDisplayNameAndDescription(String displayName, String description, boolean crumbFlag)
            throws IOException {
        Objects.requireNonNull(displayName, "displayName is not allowed to be null.");
        Objects.requireNonNull(description, "description is not allowed to be null.");
        //TODO:JDK9+ Map.of()...
        Map<String, String> params = new HashMap<>();
        params.put("displayName", displayName);
        params.put("description", description);
        // TODO: Check what the "core:apply" means?
        params.put("core:apply", "");
        params.put("Submit", "Save");
        client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
        return this;
    }

    /**
     * Update <code>displayName</code> and the <code>description</code> of a
     * build.
     * 
     * @param displayName The new displayName which should be set.
     * @param description The description which should be set.
     * @throws IOException in case of errors.
     */
    public BuildWithDetails updateDisplayNameAndDescription(String displayName, String description) throws IOException {
        return updateDisplayNameAndDescription(displayName, description, false);
    }

    /**
     * Update <code>displayName</code> of a build.
     * 
     * @param displayName The new displayName which should be set.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public BuildWithDetails updateDisplayName(String displayName, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(displayName, "displayName is not allowed to be null.");
        String description = getDescription() == null ? "" : getDescription();
        Map<String, String> params = new HashMap<>();
        params.put("displayName", displayName);
        params.put("description", description);
        // TODO: Check what the "core:apply" means?
        params.put("core:apply", "");
        params.put("Submit", "Save");
        client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
        return this;
    }

    /**
     * Update <code>displayName</code> of a build.
     * 
     * @param displayName The new displayName which should be set.
     * @throws IOException in case of errors.
     */
    public BuildWithDetails updateDisplayName(String displayName) throws IOException {
        return updateDisplayName(displayName, false);
    }

    /**
     * Update the <code>description</code> of a build.
     * 
     * @param description The description which should be set.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public BuildWithDetails updateDescription(String description, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(description, "description is not allowed to be null.");
        String displayName = getDisplayName() == null ? "" : getDisplayName();
        //JDK9+: Map.of(..)
        Map<String, String> params = new HashMap<>();
        params.put("displayName", displayName);
        params.put("description", description);
        // TODO: Check what the "core:apply" means?
        params.put("core:apply", "");
        params.put("Submit", "Save");
        client.post_form(this.getUrl() + "/configSubmit?", params, crumbFlag);
        return this;
    }

    /**
     * Update the <code>description</code> of a build.
     * 
     * @param description The description which should be set.
     * @throws IOException in case of errors.
     */
    public BuildWithDetails updateDescription(String description) throws IOException {
        return updateDescription(description, false);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private BuildCause convertToBuildCause(Map<String, Object> cause) {
        BuildCause cause_object = new BuildCause();

        // TODO: Think about it. Can this be done more simpler?
        String description = (String) cause.get("shortDescription");
        if (!isNullOrEmpty(description)) {
            cause_object.setShortDescription(description);
        }

        Integer upstreamBuild = (Integer) cause.get("upstreamBuild");
        if (upstreamBuild != null) {
            cause_object.setUpstreamBuild(upstreamBuild);
        }

        String upstreamProject = (String) cause.get("upstreamProject");
        if (!isNullOrEmpty(upstreamProject)) {
            cause_object.setUpstreamProject(upstreamProject);
        }

        String upstreamUrl = (String) cause.get("upstreamUrl");
        if (!isNullOrEmpty(upstreamUrl)) {
            cause_object.setUpstreamUrl(upstreamUrl);
        }

        String userId = (String) cause.get("userId");
        if (!isNullOrEmpty(userId)) {
            cause_object.setUserId(userId);
        }

        String userName = (String) cause.get("userName");
        if (!isNullOrEmpty(userName)) {
            cause_object.setUserName(userName);
        }
        return cause_object;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BuildResult getResult() {
        return result;
    }

    public String getBuiltOn() {
        return builtOn;
    }

    public List getActions() {
        return actions;
    }

    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = actions.stream()
                .filter(item -> item.containsKey("parameters"))
                .flatMap(item -> item.entrySet().stream())
                .flatMap(sub -> sub.getValue().stream())
                .collect(toMap(k -> (String) k.get("name"), v -> v.get("value")));

        return parameters;
    }

    /**
     * @return The full console output of the build. The line separation is done by
     *         {@code CR+LF}.
     *
     * @see #streamConsoleOutput(BuildConsoleStreamListener, int, int, boolean) method for obtaining logs for running build
     *
     * @throws IOException in case of a failure.
     */
    public String getConsoleOutputText() throws IOException {
        return client.get(getUrl() + "/logText/progressiveText");
    }

    /**
     * The full console output with HTML.
     *
     * @see #streamConsoleOutput(BuildConsoleStreamListener, int, int, boolean) method for obtaining logs for running build
     *
     * @return The console output as HTML.
     * @throws IOException in case of an error.
     */
    public String getConsoleOutputHtml() throws IOException {
        return client.get(getUrl() + "/logText/progressiveHtml");
    }


    /**
     * Stream build console output log as text using BuildConsoleStreamListener
     * Method can be used to asynchronously obtain logs for running build.
     *
     * @param listener interface used to asynchronously obtain logs
     * @param poolingInterval interval (seconds) used to pool jenkins for logs
     * @param poolingTimeout pooling timeout (seconds) used to break pooling in case build stuck
     * @throws InterruptedException in case of an error.
     * @throws IOException in case of an error.
     *
     */
    public void streamConsoleOutput(final BuildConsoleStreamListener listener, final int poolingInterval, final int poolingTimeout, boolean crumbFlag) throws InterruptedException, IOException {
        // Calculate start and timeout
        final long startTime = System.currentTimeMillis();
        final long timeoutTime = startTime + (poolingTimeout * 1000);

        int bufferOffset = 0;
        while (true) {
            Thread.sleep(poolingInterval * 1000);

            ConsoleLog consoleLog = null;
            consoleLog = getConsoleOutputText(bufferOffset, crumbFlag);
            String logString = consoleLog.getConsoleLog();
            if (logString != null && !logString.isEmpty()) {
                listener.onData(logString);
            }
            if (consoleLog.getHasMoreData()) {
                bufferOffset = consoleLog.getCurrentBufferSize();
            } else {
                listener.finished();
                break;
            }
            long currentTime = System.currentTimeMillis();

            if (currentTime > timeoutTime) {
                LOGGER.warn("Pooling for build {0} for {2} timeout! Check if job stuck in jenkins",
                        BuildWithDetails.this.getDisplayName(), BuildWithDetails.this.getNumber());
                break;
            }
        }
    }

    /**
     * Get build console output log as text.
     * Use this method to periodically obtain logs from jenkins and skip chunks that were already received
     *
     * @param bufferOffset offset in console lo
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @return ConsoleLog object containing console output of the build. The line separation is done by
     * {@code CR+LF}.
     * @throws IOException in case of a failure.
     */
    public ConsoleLog getConsoleOutputText(int bufferOffset, boolean crumbFlag) throws IOException {
        List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("start", Integer.toString(bufferOffset)));
        String path = getUrl() + "logText/progressiveText";
        HttpResponse httpResponse = client.post_form_with_result(path, formData, crumbFlag);

        Header moreDataHeader = httpResponse.getFirstHeader(MORE_DATA_HEADER);
        Header textSizeHeader = httpResponse.getFirstHeader(TEXT_SIZE_HEADER);
        String response = EntityUtils.toString(httpResponse.getEntity());
        boolean hasMoreData = false;
        if (moreDataHeader != null) {
            hasMoreData = Boolean.TRUE.toString().equals(moreDataHeader.getValue());
        }
        Integer currentBufferSize = bufferOffset;
        if (textSizeHeader != null) {
            try {
                currentBufferSize = Integer.parseInt(textSizeHeader.getValue());
            } catch (NumberFormatException e) {
                LOGGER.warn("Cannot parse buffer size for job {0} build {1}. Using current offset!", this.getDisplayName(), this.getNumber());
            }
        }
        return new ConsoleLog(response, hasMoreData, currentBufferSize);
    }


  /**
   * Returns the change set of a build if available.
   * 
   * If a build performs several scm checkouts (i.e. pipeline builds), the change set of the first
   * checkout is returned. To get the complete list of change sets for all checkouts, use
   * {@link #getChangeSets()}
   * 
   * If no checkout is performed, null is returned.
   * 
   * @return The change set of the build.
   * 
   */
    public BuildChangeSet getChangeSet() {
        BuildChangeSet result;
        if (changeSet != null) {
            result = changeSet;
        } else if (changeSets != null && !changeSets.isEmpty()) {
            result = changeSets.get(0);
        } else {
            result = null;
        }
        return result;
    }

    public BuildWithDetails setChangeSet(BuildChangeSet changeSet) {
        this.changeSet = changeSet;
        return this;
    }

  /**
   * Returns the complete list of change sets for all checkout the build has performed. If no
   * checkouts have been performed, returns null.
   * 
   * @return The complete list of change sets of the build.
   */
    public List<BuildChangeSet> getChangeSets() {
        List<BuildChangeSet> result;
        if (changeSets != null) {
            result = changeSets;
        } else if (changeSet != null) {
            result = Collections.singletonList(changeSet);
        } else {
            result = null;
	}
        return result;
    }

    public BuildWithDetails setChangeSets(List<BuildChangeSet> changeSets) {
        this.changeSets = changeSets;
        return this;
    }

    public List<BuildChangeSetAuthor> getCulprits() {
        return culprits;
    }

    public BuildWithDetails setCulprits(List<BuildChangeSetAuthor> culprits) {
        this.culprits = culprits;
        return this;
    }

    public BuildWithDetails setResult(BuildResult result) {
        this.result = result;
        return this;
    }

    public InputStream downloadArtifact(Artifact a) throws IOException, URISyntaxException {
        // We can't just put the artifact's relative path at the end of the url
        // string, as there could be characters that need to be escaped.
        URI uri = new URI(getUrl());
        String artifactPath = uri.getPath() + "artifact/" + a.getRelativePath();
        URI artifactUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), artifactPath, "",
                "");
        return client.getFile(artifactUri);
    }
    
    /**
     * Returns {@link MavenModuleWithDetails} based on its name
     * 
     * @param name module name
     * @return {@link MavenModuleWithDetails}
     * @throws IOException in case of error.
     */
    public MavenModuleWithDetails getModule(String name) throws IOException {
        return client.get(getUrl() + name, MavenModuleWithDetails.class);
    }    

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuildWithDetails other = (BuildWithDetails) obj;
        if (actions == null) {
            if (other.actions != null)
                return false;
        } else if (!actions.equals(other.actions))
            return false;
        if (artifacts == null) {
            if (other.artifacts != null)
                return false;
        } else if (!artifacts.equals(other.artifacts))
            return false;
        if (building != other.building)
            return false;
        if (builtOn == null) {
            if (other.builtOn != null)
                return false;
        } else if (!builtOn.equals(other.builtOn))
            return false;
        if (changeSet == null) {
            if (other.changeSet != null)
                return false;
        } else if (!changeSet.equals(other.changeSet))
            return false;
        if (changeSets == null) {
            if (other.changeSets != null)
                return false;
        } else if (!changeSets.equals(other.changeSets))
            return false;
        if (consoleOutputHtml == null) {
            if (other.consoleOutputHtml != null)
                return false;
        } else if (!consoleOutputHtml.equals(other.consoleOutputHtml))
            return false;
        if (consoleOutputText == null) {
            if (other.consoleOutputText != null)
                return false;
        } else if (!consoleOutputText.equals(other.consoleOutputText))
            return false;
        if (culprits == null) {
            if (other.culprits != null)
                return false;
        } else if (!culprits.equals(other.culprits))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (duration != other.duration)
            return false;
        if (estimatedDuration != other.estimatedDuration)
            return false;
        if (fullDisplayName == null) {
            if (other.fullDisplayName != null)
                return false;
        } else if (!fullDisplayName.equals(other.fullDisplayName))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (result != other.result)
            return false;
        if (timestamp != other.timestamp)
            return false;
        return true;
    }

    @Override
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
