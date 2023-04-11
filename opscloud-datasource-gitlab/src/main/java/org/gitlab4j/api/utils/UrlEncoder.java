package org.gitlab4j.api.utils;

import org.gitlab4j.api.GitLabApiException;

import java.net.URLEncoder;

public class UrlEncoder {

    /**
     * URL encodes a String in compliance with GitLabs special differences.
     *
     * @param s the String to URL encode
     * @return the URL encoded strings
     * @throws GitLabApiException if any exception occurs
     */
    public static String urlEncode(String s) throws GitLabApiException {

        try {
            String encoded = URLEncoder.encode(s, "UTF-8");
            // Since the encode method encodes plus signs as %2B,
            // we can simply replace the encoded spaces with the correct encoding here 
            encoded = encoded.replace("+", "%20");
            encoded = encoded.replace(".", "%2E");
            encoded = encoded.replace("-", "%2D");
            encoded = encoded.replace("_", "%5F");
            return (encoded);
        } catch (Exception e) {
            throw new GitLabApiException(e);
        }
    }
}
