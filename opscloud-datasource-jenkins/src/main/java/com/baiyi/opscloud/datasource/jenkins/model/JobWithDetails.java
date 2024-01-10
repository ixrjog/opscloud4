/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.client.util.EncodingUtils;
import com.baiyi.opscloud.datasource.jenkins.helper.Range;
import com.google.common.collect.Maps;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.util.*;

import static com.baiyi.opscloud.datasource.jenkins.helper.FunctionalHelper.SET_CLIENT;
import static java.util.stream.Collectors.toList;

public class JobWithDetails extends Job {

    private String description;

    private String displayName;

    private boolean buildable;

    private final List<Build> builds = Collections.emptyList();

    private Build firstBuild;

    private Build lastBuild;

    private Build lastCompletedBuild;

    private Build lastFailedBuild;

    private Build lastStableBuild;

    private Build lastSuccessfulBuild;

    private Build lastUnstableBuild;

    private Build lastUnsuccessfulBuild;

    private int nextBuildNumber;

    private boolean inQueue;

    private QueueItem queueItem;

    private List<Job> downstreamProjects;

    private List<Job> upstreamProjects;
    
    public String getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    /**
     * This method will give you back the builds of a particular job.
     * 
     * <b>Note: Jenkins limits the number of results to a maximum of 100 builds
     * which you will get back.</b>. In case you have more than 100 build you
     * won't get back all builds via this method. In such cases you need to use
     * {@link #getAllBuilds()}.
     * 
     * @return the list of {@link Build}. In case of no builds have been
     *         executed yet return {@link Collections#emptyList()}.
     */
    public List<Build> getBuilds() {
        return builds.stream()
                .map(this::buildWithClient)
                .collect(toList());
    }

    /**
     * This method will give you back all builds which exists independent of the
     * number. You should be aware that this can be much in some cases if you
     * have more than 100 builds which is by default limited by Jenkins
     * {@link #getBuilds()}. This method limits it to particular information
     * which can be later used to get supplemental information about a
     * particular build {@link Build#details()} to reduce the amount of data
     * which needed to be transfered.
     * 
     * @return the list of {@link Build}. In case of no builds have been
     *         executed yet return {@link Collections#emptyList()}.
     * @throws IOException In case of failure.
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-30238">Jenkins
     *      Issue</a>
     */
    public List<Build> getAllBuilds() throws IOException {
        String path = "/";

        try {
            List<Build> builds = client.get(path + "job/" + EncodingUtils.encode(this.getName())
                    + "?tree=allBuilds[number[*],url[*],queueId[*]]", AllBuilds.class).getAllBuilds();

            if (builds == null) {
                return Collections.emptyList();
            } else {
                return builds.stream()
                        .map(this::buildWithClient)
                        .collect(toList());
            }
        } catch (HttpResponseException e) {
            // TODO: Thinks about a better handling if the job does not exist?
            if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                // TODO: Check this if this is necessary or a good idea?

                return null;
            }
            throw e;
        }

    }

    /**
     *
     * <ul>
     * <li>{M,N}: From the M-th element (inclusive) to the N-th element
     * (exclusive).</li>
     * <li>{M,}: From the M-th element (inclusive) to the end.</li>
     * <li>{,N}: From the first element (inclusive) to the N-th element
     * (exclusive). The same as {0,N}.</li>
     * <li>{N}: Just retrieve the N-th element. The same as {N,N+1}.</li>
     * </ul>
     * 
     * <b>Note: At the moment there seemed to be no option to get the number of
     * existing builds for a job. The only option is to get all builds via
     * {@link #getAllBuilds()}.</b>
     * 
     * @param range {@link Range}
     * @return the list of {@link Build}. In case of no builds have been
     *         executed yet return {@link Collections#emptyList()}.
     * @throws IOException in case of an error.
     */
    public List<Build> getAllBuilds(Range range) throws IOException {
        String path = "/" + "job/" + EncodingUtils.encode(this.getName())
                + "?tree=allBuilds[number[*],url[*],queueId[*]]";

        try {
            List<Build> builds = client.get(path + range.getRangeString(), AllBuilds.class).getAllBuilds();

            if (builds == null) {
                return Collections.emptyList();
            } else {
                return builds.stream()
                        .map(this::buildWithClient)
                        .collect(toList());
            }
        } catch (HttpResponseException e) {
            // TODO: Thinks about a better handline if the job does not exist?
            if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                // TODO: Check this if this is necessary or a good idea?

                return null;
            }
            throw e;
        }
    }

    private Build buildWithClient(Build from) {
        Build ret = from;
        if (from != null) {
            ret = new Build(from);
            ret.setClient(client);
        }
        return ret;
    }

    /**
     * @return the first build which has been executed or
     *         {@link Build#BUILD_HAS_NEVER_RUN} if the build has never been
     *         run.
     */
    public Build getFirstBuild() {
        if (firstBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(firstBuild);
        }
    }

    /**
     * Check if the {@link #firstBuild} has been run or not.
     * 
     * @return <code>true</code> if a build has been run <code>false</code>
     *         otherwise.
     */
    public boolean hasFirstBuildRun() {
        return firstBuild != null;
    }

    /**
     * @return The lastBuild. If {@link #lastBuild} has never been run
     *         {@link Build#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public Build getLastBuild() {
        if (lastBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastBuild);
        }
    }

    /**
     * Check if the {@link #lastBuild} has been run or not.
     * 
     * @return <code>true</code> if the last build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastBuildRun() {
        return lastBuild != null;
    }

    /**
     * @return The lastCompletedBuild. If {@link #lastCompletedBuild} has never
     *         been run {@link Build#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public Build getLastCompletedBuild() {
        if (lastCompletedBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastCompletedBuild);
        }
    }

    /**
     * Check if the {@link #lastCompletedBuild} has been run or not.
     * 
     * @return <code>true</code> if the last completed build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastCompletedBuildRun() {
        return lastCompletedBuild != null;
    }

    /**
     * @return The lastFailedBuild. If {@link #lastFailedBuild} has never been
     *         run {@link Build#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public Build getLastFailedBuild() {
        if (lastFailedBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastFailedBuild);
        }
    }

    /**
     * Check if the {@link #lastFailedBuild} has been run or not.
     * 
     * @return <code>true</code> if the last failed build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastFailedBuildRun() {
        return lastFailedBuild != null;
    }

    /**
     * @return The lastStableBuild. If {@link #lastStableBuild} has never been
     *         run {@link Build#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public Build getLastStableBuild() {
        if (lastStableBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastStableBuild);
        }
    }

    /**
     * Check if the {@link #lastStableBuild} has been run or not.
     * 
     * @return <code>true</code> if the last stable build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastStableBuildRun() {
        return lastStableBuild != null;
    }

    /**
     * @return The lastSuccessfulBuild. If {@link #lastSuccessfulBuild} has
     *         never been run {@link Build#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public Build getLastSuccessfulBuild() {
        if (lastSuccessfulBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastSuccessfulBuild);
        }
    }

    /**
     * Check if the {@link #lastSuccessfulBuild} has been run or not.
     * 
     * @return <code>true</code> if the last successful build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastSuccessfulBuildRun() {
        return lastSuccessfulBuild != null;
    }

    /**
     * @return The lastUnstableBuild. If {@link #lastUnstableBuild} has never
     *         been run {@link Build#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public Build getLastUnstableBuild() {
        if (lastUnstableBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnstableBuild);
        }
    }

    /**
     * Check if the {@link #lastUnstableBuild} has been run or not.
     * 
     * @return <code>true</code> if the last unstable build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastUnstableBuildRun() {
        return lastUnstableBuild != null;
    }

    /**
     * @return The lastUnsuccessfulBuild. If {@link #lastUnsuccessfulBuild} has
     *         never been run {@link Build#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public Build getLastUnsuccessfulBuild() {
        if (lastUnsuccessfulBuild == null) {
            return Build.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnsuccessfulBuild);
        }
    }

    /**
     * Check if the {@link #lastUnsuccessfulBuild} has been run or not.
     * 
     * @return <code>true</code> if the last unsuccessful build has been run
     *         <code>false</code> otherwise.
     */
    public boolean hasLastUnsuccessfulBuildRun() {
        return lastUnsuccessfulBuild != null;
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    /**
     * @return the list of downstream projects. If no downstream projects exist
     *         just return an empty list {@link Collections#emptyList()}.
     */
    public List<Job> getDownstreamProjects() {
        if (downstreamProjects == null) {
            return Collections.emptyList();
        } else {
            return downstreamProjects.stream()
                    .map(SET_CLIENT(this.client))
                    .collect(toList());
        }
    }

    /**
     * @return the list of upstream projects. If no upstream projects exist just
     *         return an empty list {@link Collections#emptyList()}.
     */
    public List<Job> getUpstreamProjects() {
        if (upstreamProjects == null) {
            return Collections.emptyList();
        } else {
            return upstreamProjects.stream()
                    .map(SET_CLIENT(this.client))
                    .collect(toList());
        }
    }

    public QueueItem getQueueItem() {
        return this.queueItem;
    }

    /**
     * Get a build by the given buildNumber.
     * 
     * @param buildNumber The number to select the build by.
     * @return The an Optional with the {@link Build} selected by the given buildnumber
     *
     */
    public Optional<Build> getBuildByNumber(final int buildNumber) {
        return builds.stream().filter(isBuildNumberEqualTo(buildNumber)).findFirst();
    }
    
    /**
     * Get a module of a {@link Job}
     * 
     * @param moduleName name of the {@link MavenModule}
     * @return The {@link MavenModuleWithDetails} selected by the given module name
     * @throws IOException in case of errors.
     * 
     */    
    public MavenModuleWithDetails getModule(String moduleName) throws IOException {
        return client.get(getUrl() + moduleName, MavenModuleWithDetails.class);
    }    

    /**
     * Empty description to be used for {@link #updateDescription(String)} or
     * {@link #updateDescription(String, boolean)}.
     */
    public static final String EMPTY_DESCRIPTION = "";

    /**
     * Update the <code>description</code> of a Job.
     * 
     * @param description The description which should be set. If you like to
     *            set an empty description you should use
     *            {@link #EMPTY_DESCRIPTION}.
     * @throws IOException in case of errors.
     */
    public JobWithDetails updateDescription(String description) throws IOException {
        return updateDescription(description, false);
    }

    /**
     * Update the <code>description</code> of a Job.
     * 
     * @param description The description which should be set. If you like to
     *            set an empty description you should use
     *            {@link #EMPTY_DESCRIPTION}.
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public JobWithDetails updateDescription(String description, boolean crumbFlag) throws IOException {
        Objects.requireNonNull(description, "description is not allowed to be null.");
        //JDK9+
        // Map.of(...);
        Map<String, String> params = Maps.newHashMap();
        params.put("description", description);
        client.post_form(this.getUrl() + "/submitDescription?", params, crumbFlag);
        return this;
    }

    /**
     * clear the description of a job.
     * 
     * @throws IOException in case of errors.
     */
    public JobWithDetails clearDescription() throws IOException {
        return updateDescription(EMPTY_DESCRIPTION);
    }

    /**
     * clear the description of a job.
     * 
     * @param crumbFlag <code>true</code> or <code>false</code>.
     * @throws IOException in case of errors.
     */
    public JobWithDetails clearDescription(boolean crumbFlag) throws IOException {
        return updateDescription(EMPTY_DESCRIPTION, crumbFlag);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (buildable ? 1231 : 1237);
        result = prime * result + builds.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((downstreamProjects == null) ? 0 : downstreamProjects.hashCode());
        result = prime * result + ((firstBuild == null) ? 0 : firstBuild.hashCode());
        result = prime * result + (inQueue ? 1231 : 1237);
        result = prime * result + ((lastBuild == null) ? 0 : lastBuild.hashCode());
        result = prime * result + ((lastCompletedBuild == null) ? 0 : lastCompletedBuild.hashCode());
        result = prime * result + ((lastFailedBuild == null) ? 0 : lastFailedBuild.hashCode());
        result = prime * result + ((lastStableBuild == null) ? 0 : lastStableBuild.hashCode());
        result = prime * result + ((lastSuccessfulBuild == null) ? 0 : lastSuccessfulBuild.hashCode());
        result = prime * result + ((lastUnstableBuild == null) ? 0 : lastUnstableBuild.hashCode());
        result = prime * result + ((lastUnsuccessfulBuild == null) ? 0 : lastUnsuccessfulBuild.hashCode());
        result = prime * result + nextBuildNumber;
        result = prime * result + ((queueItem == null) ? 0 : queueItem.hashCode());
        result = prime * result + ((upstreamProjects == null) ? 0 : upstreamProjects.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        JobWithDetails other = (JobWithDetails) obj;
        if (buildable != other.buildable)
            return false;
        if (!builds.equals(other.builds))
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
        if (downstreamProjects == null) {
            if (other.downstreamProjects != null)
                return false;
        } else if (!downstreamProjects.equals(other.downstreamProjects))
            return false;
        if (firstBuild == null) {
            if (other.firstBuild != null)
                return false;
        } else if (!firstBuild.equals(other.firstBuild))
            return false;
        if (inQueue != other.inQueue)
            return false;
        if (lastBuild == null) {
            if (other.lastBuild != null)
                return false;
        } else if (!lastBuild.equals(other.lastBuild))
            return false;
        if (lastCompletedBuild == null) {
            if (other.lastCompletedBuild != null)
                return false;
        } else if (!lastCompletedBuild.equals(other.lastCompletedBuild))
            return false;
        if (lastFailedBuild == null) {
            if (other.lastFailedBuild != null)
                return false;
        } else if (!lastFailedBuild.equals(other.lastFailedBuild))
            return false;
        if (lastStableBuild == null) {
            if (other.lastStableBuild != null)
                return false;
        } else if (!lastStableBuild.equals(other.lastStableBuild))
            return false;
        if (lastSuccessfulBuild == null) {
            if (other.lastSuccessfulBuild != null)
                return false;
        } else if (!lastSuccessfulBuild.equals(other.lastSuccessfulBuild))
            return false;
        if (lastUnstableBuild == null) {
            if (other.lastUnstableBuild != null)
                return false;
        } else if (!lastUnstableBuild.equals(other.lastUnstableBuild))
            return false;
        if (lastUnsuccessfulBuild == null) {
            if (other.lastUnsuccessfulBuild != null)
                return false;
        } else if (!lastUnsuccessfulBuild.equals(other.lastUnsuccessfulBuild))
            return false;
        if (nextBuildNumber != other.nextBuildNumber)
            return false;
        if (queueItem == null) {
            if (other.queueItem != null)
                return false;
        } else if (!queueItem.equals(other.queueItem))
            return false;
        if (upstreamProjects == null) {
            return other.upstreamProjects == null;
        } else return upstreamProjects.equals(other.upstreamProjects);
    }

}