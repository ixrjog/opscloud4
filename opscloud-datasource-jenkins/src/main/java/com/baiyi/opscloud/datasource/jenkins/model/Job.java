/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.model;

import com.baiyi.opscloud.datasource.jenkins.client.util.EncodingUtils;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liangjian
 */
@Getter
public class Job extends BaseModel {

    private String name;
    private String url;
    private String fullName;

    public Job() {
    }

    public Job(String name, String url) {
        this();
        this.name = name;
        this.url = url;
        this.fullName = null;
    }

    public Job(String name, String url, String fullName) {
        this();
        this.name = name;
        this.url = url;
        this.fullName = fullName;
    }

    public JobWithDetails details() throws IOException {
        return client.get(url, JobWithDetails.class);
    }

    /**
     * Get a file from workspace.
     *
     * @param fileName The name of the file to download from workspace. You can
     *                 also access files which are in sub folders of the workspace.
     * @return The string which contains the content of the file.
     * @throws IOException in case of an error.
     */
    public String getFileFromWorkspace(String fileName) throws IOException {
        InputStream is = client.getFile(URI.create(url + "/ws/" + fileName));
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    /**
     * Trigger a build without parameters
     *
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build() throws IOException {
        ExtractHeader location = client.post(url + "build", null, ExtractHeader.class, false);
        return new QueueReference(location.getLocation());
    }

    /**
     * Trigger a build with crumbFlag.
     *
     * @param crumbFlag true or false.
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(boolean crumbFlag) throws IOException {
        ExtractHeader location = client.post(url + "build", null, ExtractHeader.class, crumbFlag);
        return new QueueReference(location.getLocation());
    }

    /**
     * Trigger a parameterized build with string parameters only
     *
     * @param params the job parameters
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(Map<String, String> params) throws IOException {
        return build(params, null, false);
    }

    /**
     * Trigger a parameterized build with string parameters only
     *
     * @param params    the job parameters
     * @param crumbFlag true or false.
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(Map<String, String> params, boolean crumbFlag) throws IOException {
        return build(params, null, crumbFlag);
    }

    /**
     * Trigger a parameterized build with file parameters
     *
     * @param params     the job parameters
     * @param fileParams the job file parameters
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(Map<String, String> params, Map<String, File> fileParams) throws IOException {
        return build(params, fileParams, false);
    }

    /**
     * Trigger a parameterized build with file parameters and crumbFlag
     *
     * @param params     the job parameters
     * @param fileParams the job file parameters
     * @param crumbFlag  determines whether crumb flag is used
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(Map<String, String> params, Map<String, File> fileParams, boolean crumbFlag) throws IOException {
        String qs = params.entrySet().stream()
                .map(s -> s.getKey() + "=" + s.getValue())
                .collect(Collectors.joining("&"));
//        String qs = join(Collections2.transform(params.entrySet(), new MapEntryToQueryStringPair()), "&");
        ExtractHeader location = client.post(url + "buildWithParameters?" + qs, null, ExtractHeader.class, fileParams, crumbFlag);
        return new QueueReference(location.getLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Job job = (Job) o;

        if (Optional.ofNullable(name).map(s -> !s.equals(job.name)).orElseGet(() -> job.name != null))
            return false;
        if (Optional.ofNullable(url).map(s -> !s.equals(job.url)).orElseGet(() -> job.url != null))
            return false;
        return !Optional.ofNullable(fullName).map(s -> !s.equals(job.fullName)).orElseGet(() -> job.fullName != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0) + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    private static class MapEntryToQueryStringPair implements Function<Map.Entry<String, String>, String> {
        @Override
        public String apply(Map.Entry<String, String> entry) {
            return EncodingUtils.formParameter(entry.getKey()) + "=" + EncodingUtils.formParameter(entry.getValue());
        }
    }

}