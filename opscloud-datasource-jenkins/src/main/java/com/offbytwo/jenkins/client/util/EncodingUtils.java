/*
 * Copyright (c) 2019 Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.client.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This class is a help class to centralize the
 * encoding parts which will call an appropriate library function.
 *
 * @author Karl Heinz Marbaise
 */
public final class EncodingUtils {

    private EncodingUtils() {
    } // nope

    public static String encode(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        try {
            return URLEncoder.encode(pathPart, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            // Should never happen, because that would imply that
            // the parameter StandardCharsets.UTF_8 is wrong.
            throw new IllegalArgumentException(e);
        }
    }

    public static String formParameter(String pathPart) {
        // jenkins doesn't like the + for space, use %20 instead
        try {
            return URLEncoder.encode(pathPart, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            // Should never happen, because that would imply that
            // the parameter StandardCharsets.UTF_8 is wrong.
            throw new IllegalArgumentException(e);
        }
    }

}
