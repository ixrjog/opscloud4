/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.baiyi.opscloud.datasource.jenkins.client.util;

import com.baiyi.opscloud.datasource.jenkins.model.FolderJob;

import java.net.URI;

/**
 * Utility class for manipulating API paths.
 * @author Dell Green
 */
public final class UrlUtils {
    
    /**
     * The default size to use for string buffers.
     */
    private static final int DEFAULT_BUFFER_SIZE = 64;
    
    /**
     * Utility Class.
     */
    private UrlUtils() {
        //do nothing
    }

    /**
     * Helper to create a base url in case a folder is given
     * @param folder the folder or {@code null}
     * @return The created base url.
     */
    public static String toBaseUrl(final FolderJob folder) {
        return folder == null ? "/" : folder.getUrl();
    }
    
    /**
     * Helper to create the base url for a job, with or without a given folder
     * @param folder the folder or {@code null}
     * @param jobName the name of the job.
     * @return converted base url.
     */
    public static String toJobBaseUrl(final FolderJob folder, final String jobName) {
        final StringBuilder sb = new StringBuilder(DEFAULT_BUFFER_SIZE);
        sb.append(UrlUtils.toBaseUrl(folder));
        if (sb.charAt(sb.length() - 1) != '/') {
            sb.append('/');
        }
        sb.append("job/");
        final String[] jobNameParts = jobName.split("/");

        for (int i = 0; i < jobNameParts.length; i++) {
            sb.append(EncodingUtils.encode(jobNameParts[i]));
            if (i != jobNameParts.length - 1) {
                sb.append('/');
            }
        }
        return sb.toString();
    }
    
    
    /**
     * Helper to create the base url for a view, with or without a given folder
     * @param folder the folder or {@code null}
     * @param name the of the view.
     * @return converted view url.
     */
    public static String toViewBaseUrl(final FolderJob folder, final String name) {
        final StringBuilder sb = new StringBuilder(DEFAULT_BUFFER_SIZE);
        final String base = UrlUtils.toBaseUrl(folder);
        sb.append(base);
        if (!base.endsWith("/")) {
            sb.append('/');
        }
        sb.append("view/")
          .append(EncodingUtils.encode(name));
        return sb.toString();
    }
    
    /**
     * Parses the provided job name for folders to get the full path for the job.
     * @param jobName the fullName of the job.
     * @return the path of the job including folders if present.
     */
    public static String toFullJobPath(final String jobName) {
        final String[] parts = jobName.split("/");
        if (parts.length == 1) {
            return parts[0];
        }
        final StringBuilder sb = new StringBuilder(DEFAULT_BUFFER_SIZE);
        
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i != parts.length -1) {
                sb.append("/job/");
            }
        }
        return sb.toString();
    }
    
    /**
     * Join two paths together taking into account leading/trailing slashes.
     * @param path1 the first path
     * @param path2 the second path
     * @return the joins path
     */
    public static String join(final String path1, final String path2) {
        if (path1.isEmpty() && path2.isEmpty()) {
            return "";
        }
        if (path1.isEmpty()) {
            return path2;
        }
        if (path2.isEmpty()) {
            return path1;
        }
        final StringBuilder sb = new StringBuilder(DEFAULT_BUFFER_SIZE);
        sb.append(path1);
        if (sb.charAt(sb.length() - 1) == '/') {
            sb.setLength(sb.length() - 1);
        }
        if (path2.charAt(0) != '/') {
            sb.append('/');
        }
        sb.append(path2);
        return sb.toString();
    }

    /**
     * Create a JSON URI from the supplied parameters.
     * @param uri the server URI
     * @param context the server context if any
     * @param path the specific API path 
     * @return new full URI instance
     */
    public static URI toJsonApiUri(final URI uri, final String context, final String path) {
        String p = path;
        if (!p.matches("(?i)https?://.*")) {
            p = join(context, p);
        }
 
        if (!p.contains("?")) {
            p = join(p, "api/json");
        } else {
            final String[] components = p.split("\\?", 2);
            p = join(components[0], "api/json") + "?" + components[1];
        }
        return uri.resolve("/").resolve(p.replace(" ", "%20"));
    }

    /**
     * Create a URI from the supplied parameters.
     * @param uri the server URI
     * @param context the server context if any
     * @param path the specific API path 
     * @return new full URI instance
     */
    public static URI toNoApiUri(final URI uri, final String context, final String path) {
        final String p = path.matches("(?i)https?://.*") ? path : join(context, path);
        return uri.resolve("/").resolve(p);
    }

}