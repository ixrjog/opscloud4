package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.helper.Range;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.MavenBuild;
import com.offbytwo.jenkins.model.MavenJob;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.offbytwo.jenkins.helper.FunctionalHelper.SET_CLIENT;
import static java.util.stream.Collectors.toList;

public class MavenJobWithDetails extends MavenJob {

    private String displayName;
    private boolean buildable;
    private List<com.offbytwo.jenkins.model.MavenBuild> builds;
    private com.offbytwo.jenkins.model.MavenBuild firstBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastCompletedBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastFailedBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastStableBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastSuccessfulBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastUnstableBuild;
    private com.offbytwo.jenkins.model.MavenBuild lastUnsuccessfulBuild;
    private int nextBuildNumber;
    private List<Job> downstreamProjects;
    private List<Job> upstreamProjects;

    public MavenJobWithDetails() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isBuildable() {
        return buildable;
    }

    /**
     * This method will give you back the builds of a particular job.
     * 
     * <b>Note: Jenkins limits the number of results to a maximum of 100 builds
     * which you will get back.</b>. In case you have more than 100 build you
     * won't get back all builds via this method. In such cases you need to use
     * {@link #getAllBuilds()}.
     * 
     * @return the list of {@link com.offbytwo.jenkins.model.MavenBuild}. In case of no builds have been
     *         executed yet {@link Collections#emptyList()} will be returned.
     */
    public List<com.offbytwo.jenkins.model.MavenBuild> getBuilds() {
        if (builds == null) {
            return Collections.emptyList();
        } else {
            return builds.stream()
                    .map(SET_CLIENT(this.client))
                    .collect(toList());
        }
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
     * @throws IOException
     *             In case of failure.
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-30238">Jenkins
     *      Issue</a>
     */
    public List<com.offbytwo.jenkins.model.MavenBuild> getAllBuilds() throws IOException {
        String path = "/";

        try {
            List<com.offbytwo.jenkins.model.MavenBuild> builds = client.get(path + "job/" + EncodingUtils.encode(this.getName())
                    + "?tree=allBuilds[number[*],url[*],queueId[*]]", com.offbytwo.jenkins.model.AllMavenBuilds.class).getAllBuilds();

            if (builds == null) {
                return Collections.emptyList();
            } else {
                return builds.stream()
                        .map(SET_CLIENT(this.client))
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
    * @param range
    *            {@link Range}
    * @return the list of {@link Build}. In case of no builds have been
    *         executed yet return {@link Collections#emptyList()}.
    * @throws IOException
    *             in case of an error.
    */
   public List<com.offbytwo.jenkins.model.MavenBuild> getAllBuilds(Range range) throws IOException {
       String path = "/" + "job/" + EncodingUtils.encode(this.getName())
               + "?tree=allBuilds[number[*],url[*],queueId[*]]";

       try {
           List<com.offbytwo.jenkins.model.MavenBuild> builds = client.get(path + range.getRangeString(), com.offbytwo.jenkins.model.AllMavenBuilds.class).getAllBuilds();

           if (builds == null) {
               return Collections.emptyList();
           } else {
               return builds.stream()
                       .map(SET_CLIENT(this.client))
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
    
    
    /**
     * @return The firstBuild. If {@link #firstBuild} has never been run
     *         {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getFirstBuild() {
        if (firstBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(firstBuild);
        }
    }

    /**
     * @return The lastBuild. If {@link #lastBuild} has never been run
     *         {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastBuild() {
        if (lastBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastBuild);
        }
    }

    /**
     * @return The lastCompletedBuild. If {@link #lastCompletedBuild} has never
     *         been run {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastCompletedBuild() {
        if (lastCompletedBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastCompletedBuild);
        }
    }

    /**
     * @return The lastFailedBuild. If {@link #lastFailedBuild} has never been
     *         run {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastFailedBuild() {
        if (lastFailedBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastFailedBuild);
        }
    }

    /**
     * @return The lastStableBuild. If {@link #lastStableBuild} has never been
     *         run {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastStableBuild() {
        if (lastStableBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastStableBuild);
        }
    }

    /**
     * @return The lastSuccessfulBuild. If {@link #lastSuccessfulBuild} has
     *         never been run {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastSuccessfulBuild() {
        if (lastSuccessfulBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastSuccessfulBuild);
        }
    }

    /**
     * @return The lastUnstableBuild. If {@link #lastUnstableBuild} has never
     *         been run {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastUnstableBuild() {
        if (lastUnstableBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnstableBuild);
        }
    }

    /**
     * @return The lastUnsuccessfulBuild. If {@link #lastUnsuccessfulBuild} has
     *         never been run {@link com.offbytwo.jenkins.model.MavenBuild#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public com.offbytwo.jenkins.model.MavenBuild getLastUnsuccessfulBuild() {
        if (lastUnsuccessfulBuild == null) {
            return com.offbytwo.jenkins.model.MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnsuccessfulBuild);
        }
    }

    public int getNextBuildNumber() {
        return nextBuildNumber;
    }

    public List<Job> getDownstreamProjects() {
        if (downstreamProjects == null) {
            return Collections.emptyList();
        } else {
            return downstreamProjects.stream()
                    .map(SET_CLIENT(this.client))
                    .collect(toList());
        }
    }

    public List<Job> getUpstreamProjects() {
        if (upstreamProjects == null) {
            return Collections.emptyList();
        } else {
            return upstreamProjects.stream()
                    .map(SET_CLIENT(this.client))
                    .collect(toList());
        }
    }

  /**
     * @param buildNumber The build you would like to select.
     * @return Optional which contains the {@link com.offbytwo.jenkins.model.MavenBuild}.
     */
    public Optional<com.offbytwo.jenkins.model.MavenBuild> getBuildByNumber(final int buildNumber) {
        return builds.stream()
                .filter(isBuildNumberEqualTo(buildNumber))
                .findFirst();
    }
    
    private com.offbytwo.jenkins.model.MavenBuild buildWithClient(com.offbytwo.jenkins.model.MavenBuild from) {
        com.offbytwo.jenkins.model.MavenBuild ret = new MavenBuild(from);
        ret.setClient(client);
        return ret;
    }

}
