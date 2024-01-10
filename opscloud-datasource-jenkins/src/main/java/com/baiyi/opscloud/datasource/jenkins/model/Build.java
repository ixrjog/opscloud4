/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;

public class Build extends BaseModel {

    /**
     * This will be returned by the API in cases where no build has ever been
     * executed like {@link JobWithDetails#getLastBuild()} etc. This will also
     * be returned by {@link #details()} is the build has not been run.
     */
    public static final Build BUILD_HAS_NEVER_RUN = new Build(-1, -1, "UNKNOWN") {
        @Override
        public TestReport getTestReport() {
            return TestReport.NO_TEST_REPORT;
        }

        @Override
        public BuildWithDetails details() {
            // For a build which never has been run you couldn't get
            // details about!
            return BuildWithDetails.BUILD_HAS_NEVER_RUN;
        }
    };

    /**
     * This will be returned by the API in cases where a build has been
     * cancelled.
     */
    public static final Build BUILD_HAS_BEEN_CANCELLED = new Build(-1, -1, "CANCELLED") {
        @Override
        public TestReport getTestReport() {
            return TestReport.NO_TEST_REPORT;
        }

        @Override
        public BuildWithDetails details() {
            return BuildWithDetails.BUILD_HAS_BEEN_CANCELLED;
        }
    };

    private int number;
    private int queueId;
    private String url;

    private Build(int number, int queueId, String url) {
        super();
        this.number = number;
        this.queueId = queueId;
        this.url = url;
    }

    public Build() {
    }

    public Build(Build from) {
        this(from.getNumber(), from.getUrl());
    }

    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public int getQueueId() {
        return queueId;
    }

    public String getUrl() {
        return url;
    }

    protected Build setNumber(int number) {
        this.number = number;
        return this;
    }

    protected Build setQueueId(int queueId) {
        this.queueId = queueId;
        return this;
    }

    protected Build setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return The information from Jenkins. In cases the build has never run
     * {@link #BUILD_HAS_NEVER_RUN} will be returned.
     * @throws IOException in case of an error.
     */
    public BuildWithDetails details() throws IOException {
        return client.get(url, BuildWithDetails.class);
    }

    /**
     * This is to get the information about {@link TestReport}
     * for a Maven Job type.
     *
     * @return {@link TestReport}
     * @throws IOException in case of an error.
     */
    public TestReport getTestReport() throws IOException {
        return client.get(this.getUrl() + "/testReport/?depth=1", TestReport.class);
    }

    /**
     * This is to get the information about run tests for a
     * non Maven job type.
     *
     * @return {@link TestResult}
     * @throws IOException in case of an error.
     */
    public TestResult getTestResult() throws IOException {

        return client.get(this.getUrl() + "/testReport/?depth=1", TestResult.class);
    }

    /*
     * This Change (Bad Practice) is due to inconsistencies in Jenkins various
     * versions. Jenkins changed their API from post to get and from get to post
     * and so on. For example version 1.565 is supporting stop method as a post
     * call, version 1.609.1 support it as a get call and version 1.609.2
     * support it as a post call Thus, when a get is not allowed, an 405 error
     * message is generated "Method Not Allowed" and the stop isn't executed (a
     * post is required). Changed the code in order to do a post call in case a
     * get is not allowed.
     */

    public String Stop() throws IOException {
        try {

            return client.get(url + "stop");
        } catch (HttpResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                stopPost();
                return "";
            }
            throw ex;
        }
    }

    /**
     * Stops the build which is currently in progress.  This version takes in
     * a crumbFlag.  In some cases , an error is thrown which reads
     * "No valid crumb was included in the request".  This stop method is used incase
     * those issues occur
     *
     * @param crumbFlag flag used to specify if a crumb is passed into for the request
     * @return the client url
     * @throws HttpResponseException in case of an error.
     * @throws IOException           in case of an error.
     */
    public String Stop(boolean crumbFlag) throws HttpResponseException, IOException {
        try {

            return client.get(url + "stop");
        } catch (HttpResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                stopPost(crumbFlag);
                return "";
            }
            throw ex;
        }
    }

    private void stopPost(boolean crumbFlag) throws IOException {
        client.post(url + "stop", crumbFlag);
    }

    private void stopPost() throws IOException {
        client.post(url + "stop");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Build other = (Build) obj;
        if (number != other.number)
            return false;
        if (queueId != other.queueId)
            return false;
        if (url == null) {
            return other.url == null;
        } else return url.equals(other.url);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        result = prime * result + queueId;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

}