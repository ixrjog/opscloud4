package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.client.util.EncodingUtils;
import com.baiyi.opscloud.datasource.jenkins.helper.Range;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.baiyi.opscloud.datasource.jenkins.helper.FunctionalHelper.SET_CLIENT;
import static java.util.stream.Collectors.toList;

public class MavenJobWithDetails extends MavenJob {

    private String displayName;
    private boolean buildable;
    private List<MavenBuild> builds;
    private MavenBuild firstBuild;
    private MavenBuild lastBuild;
    private MavenBuild lastCompletedBuild;
    private MavenBuild lastFailedBuild;
    private MavenBuild lastStableBuild;
    private MavenBuild lastSuccessfulBuild;
    private MavenBuild lastUnstableBuild;
    private MavenBuild lastUnsuccessfulBuild;
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
     * @return the list of {@link MavenBuild}. In case of no builds have been
     *         executed yet {@link Collections#emptyList()} will be returned.
     */
    public List<MavenBuild> getBuilds() {
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
    public List<MavenBuild> getAllBuilds() throws IOException {
        String path = "/";

        try {
            List<MavenBuild> builds = client.get(path + "job/" + EncodingUtils.encode(this.getName())
                    + "?tree=allBuilds[number[*],url[*],queueId[*]]", AllMavenBuilds.class).getAllBuilds();

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
   public List<MavenBuild> getAllBuilds(Range range) throws IOException {
       String path = "/" + "job/" + EncodingUtils.encode(this.getName())
               + "?tree=allBuilds[number[*],url[*],queueId[*]]";

       try {
           List<MavenBuild> builds = client.get(path + range.getRangeString(), AllMavenBuilds.class).getAllBuilds();

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
     *         {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getFirstBuild() {
        if (firstBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(firstBuild);
        }
    }

    /**
     * @return The lastBuild. If {@link #lastBuild} has never been run
     *         {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastBuild() {
        if (lastBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastBuild);
        }
    }

    /**
     * @return The lastCompletedBuild. If {@link #lastCompletedBuild} has never
     *         been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastCompletedBuild() {
        if (lastCompletedBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastCompletedBuild);
        }
    }

    /**
     * @return The lastFailedBuild. If {@link #lastFailedBuild} has never been
     *         run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastFailedBuild() {
        if (lastFailedBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastFailedBuild);
        }
    }

    /**
     * @return The lastStableBuild. If {@link #lastStableBuild} has never been
     *         run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastStableBuild() {
        if (lastStableBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastStableBuild);
        }
    }

    /**
     * @return The lastSuccessfulBuild. If {@link #lastSuccessfulBuild} has
     *         never been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public MavenBuild getLastSuccessfulBuild() {
        if (lastSuccessfulBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastSuccessfulBuild);
        }
    }

    /**
     * @return The lastUnstableBuild. If {@link #lastUnstableBuild} has never
     *         been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be returned.
     */
    public MavenBuild getLastUnstableBuild() {
        if (lastUnstableBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
        } else {
            return buildWithClient(lastUnstableBuild);
        }
    }

    /**
     * @return The lastUnsuccessfulBuild. If {@link #lastUnsuccessfulBuild} has
     *         never been run {@link MavenBuild#BUILD_HAS_NEVER_RUN} will be
     *         returned.
     */
    public MavenBuild getLastUnsuccessfulBuild() {
        if (lastUnsuccessfulBuild == null) {
            return MavenBuild.BUILD_HAS_NEVER_RUN;
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
     * @return Optional which contains the {@link MavenBuild}.
     */
    public Optional<MavenBuild> getBuildByNumber(final int buildNumber) {
        return builds.stream()
                .filter(isBuildNumberEqualTo(buildNumber))
                .findFirst();
    }
    
    private MavenBuild buildWithClient(MavenBuild from) {
        MavenBuild ret = new MavenBuild(from);
        ret.setClient(client);
        return ret;
    }

}