/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representation of a Guacamole protocol version. Convenience methods are
 * provided for parsing and comparing versions, as is necessary when
 * determining the version of the Guacamole protocol common to guacd and a
 * client.
 *
 * @param major The major version component of the protocol version.
 *              -- GETTER --
 *              Return the major version component of the protocol version.
 * @param minor The minor version component of the protocol version.
 *              -- GETTER --
 *              Return the minor version component of the protocol version.
 * @param patch The patch version component of the protocol version.
 *              -- GETTER --
 *              Return the patch version component of the protocol version.
 */
public record GuacamoleProtocolVersion(int major, int minor, int patch) {

    /**
     * Protocol version 1.0.0 and older.  Any client that doesn't explicitly
     * set the protocol version will negotiate down to this protocol version.
     * This requires that handshake instructions be ordered correctly, and
     * lacks support for certain protocol-related features introduced in later
     * versions.
     */
    public static final GuacamoleProtocolVersion VERSION_1_0_0 = new GuacamoleProtocolVersion(1, 0, 0);

    /**
     * Protocol version 1.1.0, which introduces Client-Server version
     * detection, arbitrary handshake instruction order, and support
     * for passing the client timezone to the server during the handshake.
     */
    public static final GuacamoleProtocolVersion VERSION_1_1_0 = new GuacamoleProtocolVersion(1, 1, 0);

    /**
     * Protocol version 1.3.0, which introduces the "required" instruction
     * allowing the server to explicitly request connection parameters from the
     * client.
     */
    public static final GuacamoleProtocolVersion VERSION_1_3_0 = new GuacamoleProtocolVersion(1, 3, 0);

    /**
     * Protocol version 1.5.0, which introduces the "msg" instruction, allowing
     * the server to send message notifications that will be displayed on the
     * client. The version also adds support for the "name" handshake
     * instruction, allowing guacd to store the name of the user who is
     * accessing the connection.
     */
    public static final GuacamoleProtocolVersion VERSION_1_5_0 = new GuacamoleProtocolVersion(1, 5, 0);

    /**
     * The most recent version of the Guacamole protocol at the time this
     * version of GuacamoleProtocolVersion was built.
     */
    public static final GuacamoleProtocolVersion LATEST = VERSION_1_5_0;

    /**
     * A regular expression that matches the VERSION_X_Y_Z pattern, where
     * X is the major version component, Y is the minor version component,
     * and Z is the patch version component.  This expression puts each of
     * the version components in their own group so that they can be easily
     * used later.
     */
    private static final Pattern VERSION_PATTERN =
            Pattern.compile("^VERSION_([0-9]+)_([0-9]+)_([0-9]+)$");

    /**
     * Generate a new GuacamoleProtocolVersion object with the given
     * major version, minor version, and patch version.
     *
     * @param major The integer representation of the major version component.
     * @param minor The integer representation of the minor version component.
     * @param patch The integer representation of the patch version component.
     */
    public GuacamoleProtocolVersion {
    }

    /**
     * Returns whether this GuacamoleProtocolVersion is at least as recent as
     * (greater than or equal to) the given version.
     *
     * @param otherVersion The version to which this GuacamoleProtocolVersion should be compared.
     * @return true if this object is at least as recent as the given version,
     * false if the given version is newer.
     */
    public boolean atLeast(GuacamoleProtocolVersion otherVersion) {

        // If major is not the same, return inequality
        if (major != otherVersion.major())
            return this.major > otherVersion.major();

        // Major is the same, but minor is not, return minor inequality
        if (minor != otherVersion.minor())
            return this.minor > otherVersion.minor();

        // Major and minor are equal, so return patch inequality
        return patch >= otherVersion.patch();

    }

    /**
     * Parse the String format of the version provided and return the
     * the enum value matching that version.  If no value is provided, return
     * null.
     *
     * @param version The String format of the version to parse.
     * @return The enum value that matches the specified version, VERSION_1_0_0
     * if no match is found, or null if no comparison version is provided.
     */
    public static GuacamoleProtocolVersion parseVersion(String version) {

        // Validate format of version string
        Matcher versionMatcher = VERSION_PATTERN.matcher(version);
        if (!versionMatcher.matches())
            return null;

        // Parse version number from version string
        return new GuacamoleProtocolVersion(
                Integer.parseInt(versionMatcher.group(1)),
                Integer.parseInt(versionMatcher.group(2)),
                Integer.parseInt(versionMatcher.group(3))
        );

    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof GuacamoleProtocolVersion otherVersion))
            return false;

        // Versions are equal if all major/minor/patch components are identical
        return this.major == otherVersion.major()
                && this.minor == otherVersion.minor()
                && this.patch == otherVersion.patch();

    }

    @Override
    public String toString() {
        return "VERSION_" + major() + "_" + minor() + "_" + patch();
    }

}